
package com.example.mongo2sp.executor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.expr.*;
import com.example.mongo2sp.model.VisualizationResponse.*;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;



public class HybridExecutor {

    private static final Logger logger = LoggerFactory.getLogger(HybridExecutor.class);

    private final String sourceCode;
    private final String input;
    private final List<ExecutionFrame> frames = new ArrayList<>();
    private final Map<String, HeapObject> heap = new LinkedHashMap<>();
    private final StringBuilder output = new StringBuilder();
    private final Deque<CallContext> callStack = new ArrayDeque<>();
    private int stepNo = 0;
    private int heapCounter = 0;

    private static ThreadLocal<HybridExecutor> currentExecutor = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Object>> localVariables = ThreadLocal.withInitial(LinkedHashMap::new);

    private static class CallContext {
        String className;
        String methodName;
        int currentLine;
        Map<String, Object[]> locals;

        CallContext(String className, String methodName, int line) {
            this.className = className;
            this.methodName = methodName;
            this.currentLine = line;
            this.locals = new LinkedHashMap<>();
        }
    }

    public HybridExecutor(String sourceCode, String input) {
        logger.info("start of hybrid executor Constructor");
        this.sourceCode = sourceCode;
        this.input = input;
        logger.info("end of hybrid executor Constructor");
    }

    public List<ExecutionFrame> execute() throws Exception {
        logger.info("start of execute func");
        try {
            String className = extractClassName(sourceCode);
            logger.info("extracted className..." + sourceCode);

            CompilationUnit cu = new JavaParser().parse(sourceCode).getResult()
                    .orElseThrow(() -> new Exception("Failed to parse source code"));

            logger.info("Successfully Parsed Java Code to AST...");

            String instrumentedCode = instrumentCode(sourceCode, cu);

            Class<?> clazz = compileAndLoad(className, instrumentedCode);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Void> future = executor.submit(() -> {
                currentExecutor.set(this);
                executeInstrumented(clazz, className);
                return null;
            });

            try {
                future.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                throw new RuntimeException("Execution timeout: exceeded 10 seconds");
            } finally {
                executor.shutdownNow();
            }

        }
//        finally {
//            currentExecutor.remove();
//            localVariables.remove();
//        }
        finally {
            currentExecutor.remove();
            localVariables.remove();
            try {
                String className = extractClassName(sourceCode);

                File sourceFile = new File(className + ".java");
                if (sourceFile.exists()) sourceFile.delete();

                File classFile = new File(className + ".class");
                if (classFile.exists()) classFile.delete();

                // Optional: Delete all remaining .class files in working directory
                File[] extraFiles = new File(".").listFiles((dir, name) -> name.endsWith(".class"));
                if (extraFiles != null) {
                    for (File f : extraFiles) f.delete();
                }

                logger.info("end of execute func");

            } catch (Exception cleanupEx) {
                // Ignore cleanup errors
            }
        }

        return frames;
    }

    private String instrumentCode(String originalCode, CompilationUnit cu) {
        logger.info("Started Code Instrumentation, Inside instrumentCode Method....");
        StringBuilder instrumented = new StringBuilder();

        if (cu.getPackageDeclaration().isPresent()) {
            instrumented.append(cu.getPackageDeclaration().get().toString()).append("\n");
        }

        for (var importDecl : cu.getImports()) {
            instrumented.append(importDecl.toString()).append("\n");
        }

        instrumented.append("\n");

        String mainClassName = null;
        for (var typeDecl : cu.getTypes()) {
            if (typeDecl instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) typeDecl;
                for (var member : classDecl.getMembers()) {
                    if (member instanceof MethodDeclaration) {
                        MethodDeclaration method = (MethodDeclaration) member;
                        if (method.getNameAsString().equals("main") &&
                                method.isStatic() &&
                                method.isPublic()) {
                            mainClassName = classDecl.getNameAsString();
                            break;
                        }
                    }
                }
                if (mainClassName != null) break;
            }
        }

        if (mainClassName == null && !cu.getTypes().isEmpty()) {
            mainClassName = cu.getTypes().get(0).getNameAsString();
        }

        for (var typeDecl : cu.getTypes()) {
            if (typeDecl instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) typeDecl;
                boolean isMainClass = classDecl.getNameAsString().equals(mainClassName);
                instrumentClass(instrumented, classDecl, isMainClass);
            }
        }

        logger.info("Code Instrumentation Done.. Exiting instrumentCode Method.... " + instrumented.toString());

        return instrumented.toString();
    }

    private void instrumentClass(StringBuilder sb, ClassOrInterfaceDeclaration classDecl, boolean isPublic) {
        if (isPublic) {
            sb.append("public class ").append(classDecl.getNameAsString()).append(" {\n");
        } else {
            sb.append("class ").append(classDecl.getNameAsString()).append(" {\n");
        }

        for (var member : classDecl.getMembers()) {
            if (member instanceof FieldDeclaration) {
                sb.append("    ").append(member.toString()).append("\n");
            }
        }

        for (var member : classDecl.getMembers()) {
            if (member instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) member;
                instrumentMethod(sb, method, classDecl.getNameAsString());
            } else if (member instanceof ConstructorDeclaration) {
                ConstructorDeclaration constructor = (ConstructorDeclaration) member;
                instrumentConstructor(sb, constructor, classDecl.getNameAsString());
            }
        }

        sb.append("}\n");
    }

    private void instrumentMethod(StringBuilder sb, MethodDeclaration method, String className) {
        sb.append("    ");
        if (method.isPublic()) sb.append("public ");
        if (method.isPrivate()) sb.append("private ");
        if (method.isProtected()) sb.append("protected ");
        if (method.isStatic()) sb.append("static ");

        String returnType = method.getType().asString();
        sb.append(returnType).append(" ");
        sb.append(method.getNameAsString()).append("(");

        List<String> paramNames = new ArrayList<>();
        for (int i = 0; i < method.getParameters().size(); i++) {
            var param = method.getParameters().get(i);
            if (i > 0) sb.append(", ");
            sb.append(param.getType().asString()).append(" ").append(param.getNameAsString());
            paramNames.add(param.getNameAsString());
        }
        sb.append(") {\n");

        int startLine = method.getBegin().map(pos -> pos.line).orElse(0);
        sb.append("        com.example.mongo2sp.executor.HybridExecutor.onMethodEnter(\"")
                .append(className).append("\", \"")
                .append(method.getNameAsString()).append("\", ").append(startLine);

        if (!paramNames.isEmpty()) {
            sb.append(", new Object[]{");
            for (int i = 0; i < paramNames.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(paramNames.get(i));
            }
            sb.append("}");
        }
        sb.append(");\n");

        if (method.getBody().isPresent()) {
            BlockStmt body = method.getBody().get();
            instrumentBlock(sb, body, className, method.getNameAsString(), returnType, 2);
        }

        sb.append("    }\n");
    }

    private void instrumentConstructor(StringBuilder sb, ConstructorDeclaration constructor, String className) {
        sb.append("    public ").append(constructor.getNameAsString()).append("(");

        List<String> paramNames = new ArrayList<>();
        for (int i = 0; i < constructor.getParameters().size(); i++) {
            var param = constructor.getParameters().get(i);
            if (i > 0) sb.append(", ");
            sb.append(param.getType().asString()).append(" ").append(param.getNameAsString());
            paramNames.add(param.getNameAsString());
        }
        sb.append(") {\n");

        instrumentBlock(sb, constructor.getBody(), className, "<init>", "void", 2);

        sb.append("    }\n");
    }

    private void instrumentBlock(StringBuilder sb, BlockStmt block, String className,
                                 String methodName, String returnType, int indent) {
        for (var stmt : block.getStatements()) {
            instrumentStatement(sb, stmt, className, methodName, returnType, indent);
        }
    }

    private void instrumentStatement(StringBuilder sb, Statement stmt, String className,
                                     String methodName, String returnType, int indent) {
        int lineNo = stmt.getBegin().map(pos -> pos.line).orElse(0);

        if (stmt instanceof ExpressionStmt) {
            ExpressionStmt exprStmt = (ExpressionStmt) stmt;
            Expression expr = exprStmt.getExpression();

            if (expr instanceof VariableDeclarationExpr) {
                VariableDeclarationExpr varDecl = (VariableDeclarationExpr) expr;
                sb.append("    ".repeat(indent)).append(varDecl.toString()).append(";\n");

                for (var variable : varDecl.getVariables()) {
                    sb.append("    ".repeat(indent));
                    sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                            .append(variable.getNameAsString()).append("\", ")
                            .append(variable.getNameAsString()).append(");\n");
                }

                sb.append("    ".repeat(indent));
                sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            } else if (expr instanceof AssignExpr) {
                AssignExpr assign = (AssignExpr) expr;
                sb.append("    ".repeat(indent)).append(assign.toString()).append(";\n");

                if (assign.getTarget() instanceof NameExpr) {
                    String varName = ((NameExpr) assign.getTarget()).getNameAsString();
                    sb.append("    ".repeat(indent));
                    sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                            .append(varName).append("\", ").append(varName).append(");\n");
                } else if (assign.getTarget() instanceof ArrayAccessExpr) {
                    ArrayAccessExpr arrayAccess = (ArrayAccessExpr) assign.getTarget();
                    if (arrayAccess.getName() instanceof NameExpr) {
                        String arrayName = ((NameExpr) arrayAccess.getName()).getNameAsString();
                        sb.append("    ".repeat(indent));
                        sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                                .append(arrayName).append("\", ").append(arrayName).append(");\n");
                    }
                }

                sb.append("    ".repeat(indent));
                sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            } else if (expr instanceof UnaryExpr) {
                UnaryExpr unary = (UnaryExpr) expr;
                sb.append("    ".repeat(indent)).append(unary.toString()).append(";\n");

                if (unary.getExpression() instanceof NameExpr) {
                    String varName = ((NameExpr) unary.getExpression()).getNameAsString();
                    sb.append("    ".repeat(indent));
                    sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                            .append(varName).append("\", ").append(varName).append(");\n");
                }

                sb.append("    ".repeat(indent));
                sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            } else {
                sb.append("    ".repeat(indent)).append(stmt.toString()).append("\n");

                sb.append("    ".repeat(indent));
                sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");
            }
        } else if (stmt instanceof IfStmt) {
            sb.append("    ".repeat(indent));
            sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            IfStmt ifStmt = (IfStmt) stmt;
            sb.append("    ".repeat(indent)).append("if (").append(ifStmt.getCondition()).append(") {\n");

            if (ifStmt.getThenStmt() instanceof BlockStmt) {
                instrumentBlock(sb, (BlockStmt) ifStmt.getThenStmt(), className, methodName, returnType, indent + 1);
            } else {
                instrumentStatement(sb, ifStmt.getThenStmt(), className, methodName, returnType, indent + 1);
            }

            sb.append("    ".repeat(indent)).append("}");

            if (ifStmt.getElseStmt().isPresent()) {
                sb.append(" else {\n");
                Statement elseStmt = ifStmt.getElseStmt().get();
                if (elseStmt instanceof BlockStmt) {
                    instrumentBlock(sb, (BlockStmt) elseStmt, className, methodName, returnType, indent + 1);
                } else {
                    instrumentStatement(sb, elseStmt, className, methodName, returnType, indent + 1);
                }
                sb.append("    ".repeat(indent)).append("}");
            }
            sb.append("\n");
        } else if (stmt instanceof ForStmt) {
            ForStmt forStmt = (ForStmt) stmt;

            sb.append("    ".repeat(indent)).append("for (");

            if (!forStmt.getInitialization().isEmpty()) {
                for (int i = 0; i < forStmt.getInitialization().size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(forStmt.getInitialization().get(i).toString());
                }
            }
            sb.append("; ");

            if (forStmt.getCompare().isPresent()) {
                sb.append(forStmt.getCompare().get().toString());
            }
            sb.append("; ");

            if (!forStmt.getUpdate().isEmpty()) {
                for (int i = 0; i < forStmt.getUpdate().size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(forStmt.getUpdate().get(i).toString());
                }
            }
            sb.append(") {\n");

            // Track initialization variables right after loop starts
            if (!forStmt.getInitialization().isEmpty()) {
                for (Expression init : forStmt.getInitialization()) {
                    if (init instanceof VariableDeclarationExpr) {
                        VariableDeclarationExpr varDecl = (VariableDeclarationExpr) init;
                        for (var variable : varDecl.getVariables()) {
                            String varName = variable.getNameAsString();
                            sb.append("    ".repeat(indent + 1));
                            sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                                    .append(varName).append("\", ").append(varName).append(");\n");
                        }
                    }
                }
            }

            // Track line
            sb.append("    ".repeat(indent + 1));
            sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            // Instrument loop body
            if (forStmt.getBody() instanceof BlockStmt) {
                BlockStmt body = (BlockStmt) forStmt.getBody();
                for (var bodyStmt : body.getStatements()) {
                    instrumentStatement(sb, bodyStmt, className, methodName, returnType, indent + 1);
                }
            } else {
                instrumentStatement(sb, forStmt.getBody(), className, methodName, returnType, indent + 1);
            }

            // Track update variables at end of each iteration
            if (!forStmt.getUpdate().isEmpty()) {
                for (Expression update : forStmt.getUpdate()) {
                    if (update instanceof UnaryExpr) {
                        UnaryExpr unary = (UnaryExpr) update;
                        if (unary.getExpression() instanceof NameExpr) {
                            String varName = ((NameExpr) unary.getExpression()).getNameAsString();
                            sb.append("    ".repeat(indent + 1));
                            sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                                    .append(varName).append("\", ").append(varName).append(");\n");
                        }
                    } else if (update instanceof AssignExpr) {
                        AssignExpr assign = (AssignExpr) update;
                        if (assign.getTarget() instanceof NameExpr) {
                            String varName = ((NameExpr) assign.getTarget()).getNameAsString();
                            sb.append("    ".repeat(indent + 1));
                            sb.append("com.example.mongo2sp.executor.HybridExecutor.trackVariable(\"")
                                    .append(varName).append("\", ").append(varName).append(");\n");
                        }
                    }
                }
            }

            sb.append("    ".repeat(indent)).append("}\n");
        } else if (stmt instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) stmt;

            sb.append("    ".repeat(indent));
            sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            sb.append("    ".repeat(indent)).append("while (").append(whileStmt.getCondition()).append(") {\n");

            if (whileStmt.getBody() instanceof BlockStmt) {
                instrumentBlock(sb, (BlockStmt) whileStmt.getBody(), className, methodName, returnType, indent + 1);
            } else {
                instrumentStatement(sb, whileStmt.getBody(), className, methodName, returnType, indent + 1);
            }

            sb.append("    ".repeat(indent)).append("}\n");
        } else if (stmt instanceof DoStmt) {
            DoStmt doStmt = (DoStmt) stmt;

            sb.append("    ".repeat(indent));
            sb.append("com.example.mongo2sp.executor.HybridExecutor.onLine(").append(lineNo).append(");\n");

            sb.append("    ".repeat(indent)).append("do {\n");

            if (doStmt.getBody() instanceof BlockStmt) {
                instrumentBlock(sb, (BlockStmt) doStmt.getBody(), className, methodName, returnType, indent + 1);
            } else {
                instrumentStatement(sb, doStmt.getBody(), className, methodName, returnType, indent + 1);
            }

            sb.append("    ".repeat(indent)).append("} while (").append(doStmt.getCondition()).append(");\n");
        } else if (stmt instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) stmt;
            if (returnStmt.getExpression().isPresent()) {
                String returnExpr = returnStmt.getExpression().get().toString();

                if (returnType.equals("int") || returnType.equals("long") ||
                        returnType.equals("double") || returnType.equals("float") ||
                        returnType.equals("boolean") || returnType.equals("char") ||
                        returnType.equals("byte") || returnType.equals("short")) {
                    sb.append("    ".repeat(indent)).append("{\n");
                    sb.append("    ".repeat(indent + 1)).append(returnType).append(" __returnValue = ")
                            .append(returnExpr).append(";\n");
                    sb.append("    ".repeat(indent + 1))
                            .append("com.example.mongo2sp.executor.HybridExecutor.onMethodExit(\"")
                            .append(className).append("\", \"")
                            .append(methodName).append("\", ").append(lineNo).append(", __returnValue);\n");
                    sb.append("    ".repeat(indent + 1)).append("return __returnValue;\n");
                    sb.append("    ".repeat(indent)).append("}\n");
                } else {
                    sb.append("    ".repeat(indent)).append("{\n");
                    sb.append("    ".repeat(indent + 1)).append("Object __returnValue = ")
                            .append(returnExpr).append(";\n");
                    sb.append("    ".repeat(indent + 1))
                            .append("com.example.mongo2sp.executor.HybridExecutor.onMethodExit(\"")
                            .append(className).append("\", \"")
                            .append(methodName).append("\", ").append(lineNo).append(", __returnValue);\n");
                    sb.append("    ".repeat(indent + 1)).append("return (").append(returnType)
                            .append(") __returnValue;\n");
                    sb.append("    ".repeat(indent)).append("}\n");
                }
            } else {
                sb.append("    ".repeat(indent))
                        .append("com.example.mongo2sp.executor.HybridExecutor.onMethodExit(\"")
                        .append(className).append("\", \"")
                        .append(methodName).append("\", ").append(lineNo).append(");\n");
                sb.append("    ".repeat(indent)).append("return;\n");
            }
        } else {
            sb.append("    ".repeat(indent)).append(stmt.toString()).append("\n");
        }
    }

    private void executeInstrumented(Class<?> clazz, String className) throws Exception {
        logger.info("executeInstrumented started....");
        currentExecutor.set(this);

        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        InputStream originalIn = System.in;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(baos, true) {
            @Override
            public void println(String x) {
                output.append(x).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void print(String x) {
                output.append(x);
                super.print(x);
                super.flush();
            }

            @Override
            public void println(int x) {
                output.append(String.valueOf(x)).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void print(int x) {
                output.append(String.valueOf(x));
                super.print(x);
                super.flush();
            }

            @Override
            public void println(long x) {
                output.append(String.valueOf(x)).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void println(double x) {
                output.append(String.valueOf(x)).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void println(boolean x) {
                output.append(String.valueOf(x)).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void println(char x) {
                output.append(String.valueOf(x)).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void println(Object x) {
                output.append(String.valueOf(x)).append("\n");
//                super.println(x);
                super.flush();
            }

            @Override
            public void println() {
                output.append("\n");
                super.println();
                super.flush();
            }
        };

        System.setOut(captureStream);
        System.setErr(captureStream);

        if (input != null && !input.isEmpty()) {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
        }

        try {
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[]{});

            captureStream.flush();
            Thread.sleep(50);

        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new RuntimeException(cause != null ? cause.getMessage() : "Execution error");
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
            System.setIn(originalIn);
        }
        logger.info("executeInstrumented ended....");
    }

    public static void onMethodEnter(String className, String methodName, int lineNo, Object... params) {
        logger.info("onMethodEnter started....");

        HybridExecutor executor = currentExecutor.get();
        if (executor == null) return;

        CallContext ctx = new CallContext(className, methodName, lineNo);

        if (params != null && params.length > 0) {
            if (methodName.equals("main") && params.length == 1 && params[0] instanceof String[]) {
                ctx.locals.put("args", executor.valueToFrame(params[0]));
            } else {
                for (int i = 0; i < params.length; i++) {
                    ctx.locals.put("param" + i, executor.valueToFrame(params[i]));
                }
            }
        }

        executor.callStack.push(ctx);
        executor.addCallFrame(className, methodName, lineNo);
        logger.info("onMethodEnter ended....");
    }

    public static void onLine(int lineNo) {

        logger.info("Entered in method in OnLine : " + lineNo);

        HybridExecutor executor = currentExecutor.get();
        if (executor == null || executor.callStack.isEmpty()) return;

        CallContext ctx = executor.callStack.peek();
        int lastLine = ctx.currentLine;
        ctx.currentLine = lineNo;

        Map<String, Object> vars = localVariables.get();
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            ctx.locals.put(entry.getKey(), executor.valueToFrame(entry.getValue()));
        }

        executor.addLineFrame(lastLine, lineNo);

        logger.info("Ended method OnLine for Line No. : " + lineNo);
    }

    public static void trackVariable(String name, Object value) {
        localVariables.get().put(name, value);
    }

    public static void onMethodExit(String className, String methodName, int lineNo) {
        onMethodExit(className, methodName, lineNo, null);
    }

    public static void onMethodExit(String className, String methodName, int lineNo, Object returnValue) {
        HybridExecutor executor = currentExecutor.get();
        if (executor == null) return;

        executor.addReturnFrame(className, methodName, lineNo, returnValue);

        if (!executor.callStack.isEmpty() && executor.callStack.peek().methodName.equals(methodName)) {
            executor.callStack.poll();
        }

        localVariables.get().clear();
    }

    private void addCallFrame(String className, String methodName, int lineNo) {
        ExecutionFrame frame = createFrame("call", 0, lineNo);
        logger.info("addCallFrame : " + frames.size());
        frames.add(frame);
    }

    private void addLineFrame(int lastLine, int nextLine) {
        ExecutionFrame frame = createFrame("line", lastLine, nextLine);
        frames.add(frame);
    }

    private void addReturnFrame(String className, String methodName, int lineNo, Object returnValue) {
        ExecutionFrame frame = createFrame("return", lineNo, lineNo);

        if (!frame.getFrameList().isEmpty()) {
            FrameInfo topFrame = frame.getFrameList().get(0);
            topFrame.set_return(valueToFrame(returnValue));
        }

        frames.add(frame);
    }

    private ExecutionFrame createFrame(String event, int lastLine, int nextLine) {
        ExecutionFrame frame = new ExecutionFrame();
        frame.setEvent(event);
        frame.setErrorMessage("");
        frame.setStepNo(++stepNo);
        frame.setLastLineNo(String.valueOf(lastLine));
        frame.setNextLineNo(String.valueOf(nextLine));
        frame.setPrintedString(output.toString());

        List<FrameInfo> frameList = new ArrayList<>();
        for (CallContext ctx : callStack) {
            FrameInfo fi = new FrameInfo();
            fi.setObjectName(ctx.className + "." + ctx.methodName);
            fi.setLineno(ctx.currentLine);
            fi.setClassName(ctx.className);
            fi.setLocals(new LinkedHashMap<>(ctx.locals));
            frameList.add(fi);
        }
        frame.setFrameList(frameList);

        if (!callStack.isEmpty()) {
            CallContext ctx = callStack.peek();
            frame.setCurrentObjectName(ctx.className + "." + ctx.methodName +
                    (ctx.methodName.equals("main") ? "(java.lang.String[])" : "()"));
        }

        frame.setHeap(new LinkedHashMap<>(heap));

        return frame;
    }

    private Object[] valueToFrame(Object value) {
        if (value == null) {
            return new Object[]{"VALUE", "null"};
        }

        Class<?> type = value.getClass();

        if (type.isPrimitive() || value instanceof Number || value instanceof Boolean ||
                value instanceof Character || value instanceof String) {
            return new Object[]{"VALUE", String.valueOf(value)};
        }

        if (type.isArray()) {
            String heapId = String.valueOf(heapCounter++);
            HeapObject heapObj = new HeapObject();
            heapObj.setType("ARRAY");

            int length = Array.getLength(value);
            heapObj.setLength(length);

            List<Object[]> elements = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Object elem = Array.get(value, i);
                elements.add(valueToFrame(elem));
            }
            heapObj.setElements(elements);

            heap.put(heapId, heapObj);
            return new Object[]{"REF", heapId};
        }

        // Collections - ArrayList, LinkedList, etc.
        if (value instanceof java.util.List) {
            String heapId = String.valueOf(heapCounter++);
            HeapObject heapObj = new HeapObject();
            heapObj.setType("LIST");

            java.util.List<?> list = (java.util.List<?>) value;
            heapObj.setLength(list.size());

            List<Object[]> elements = new ArrayList<>();
            for (Object elem : list) {
                elements.add(valueToFrame(elem));
            }
            heapObj.setElements(elements);

            heap.put(heapId, heapObj);
            return new Object[]{"REF", heapId};
        }

        // Map - HashMap, TreeMap, etc.
        if (value instanceof java.util.Map) {
            String heapId = String.valueOf(heapCounter++);
            HeapObject heapObj = new HeapObject();
            heapObj.setType("MAP");

            java.util.Map<?, ?> map = (java.util.Map<?, ?>) value;
            heapObj.setLength(map.size());

            Map<String, Object[]> fields = new LinkedHashMap<>();
            for (java.util.Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                fields.put(key, valueToFrame(entry.getValue()));
            }
            heapObj.setFields(fields);

            heap.put(heapId, heapObj);
            return new Object[]{"REF", heapId};
        }

        // Set - HashSet, TreeSet, etc.
        if (value instanceof java.util.Set) {
            String heapId = String.valueOf(heapCounter++);
            HeapObject heapObj = new HeapObject();
            heapObj.setType("SET");

            java.util.Set<?> set = (java.util.Set<?>) value;
            heapObj.setLength(set.size());

            List<Object[]> elements = new ArrayList<>();
            for (Object elem : set) {
                elements.add(valueToFrame(elem));
            }
            heapObj.setElements(elements);

            heap.put(heapId, heapObj);
            return new Object[]{"REF", heapId};
        }

        // Queue, Deque, Stack
        if (value instanceof java.util.Queue || value instanceof java.util.Deque ||
                value instanceof java.util.Stack) {
            String heapId = String.valueOf(heapCounter++);
            HeapObject heapObj = new HeapObject();
            heapObj.setType("QUEUE");

            java.util.Collection<?> collection = (java.util.Collection<?>) value;
            heapObj.setLength(collection.size());

            List<Object[]> elements = new ArrayList<>();
            for (Object elem : collection) {
                elements.add(valueToFrame(elem));
            }
            heapObj.setElements(elements);

            heap.put(heapId, heapObj);
            return new Object[]{"REF", heapId};
        }

        // StringBuffer and StringBuilder - show as string value
        if (value instanceof StringBuffer || value instanceof StringBuilder) {
            return new Object[]{"VALUE", value.toString()};
        }

        // StringTokenizer - show token count and remaining tokens
        if (value instanceof java.util.StringTokenizer) {
            String heapId = String.valueOf(heapCounter++);
            HeapObject heapObj = new HeapObject();
            heapObj.setType("TOKENIZER");

            java.util.StringTokenizer tokenizer = (java.util.StringTokenizer) value;
            int tokenCount = tokenizer.countTokens();
            heapObj.setLength(tokenCount);

            List<Object[]> tokens = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                tokens.add(new Object[]{"VALUE", tokenizer.nextToken()});
            }
            heapObj.setElements(tokens);

            heap.put(heapId, heapObj);
            return new Object[]{"REF", heapId};
        }

        // Regular Objects
        String heapId = String.valueOf(heapCounter++);
        HeapObject heapObj = new HeapObject();
        heapObj.setType("OBJECT");
        heapObj.setFields(new LinkedHashMap<>());

        try {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                if (field.isSynthetic()) continue;

                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(value);
                    heapObj.getFields().put(field.getName(), valueToFrame(fieldValue));
                } catch (Exception e) {
                    // Skip inaccessible fields
                }
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }

        heap.put(heapId, heapObj);
        return new Object[]{"REF", heapId};
    }

    private String extractClassName(String code) {
        logger.info("Extracting className");
        java.util.regex.Pattern mainPattern = java.util.regex.Pattern.compile(
                "public\\s+class\\s+(\\w+)\\s*\\{[^}]*public\\s+static\\s+void\\s+main",
                java.util.regex.Pattern.DOTALL);
        java.util.regex.Matcher mainMatcher = mainPattern.matcher(code);
        if (mainMatcher.find()) {
            return mainMatcher.group(1);
        }

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "public\\s+class\\s+(\\w+)");
        java.util.regex.Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }

        java.util.regex.Pattern anyClassPattern = java.util.regex.Pattern.compile(
                "class\\s+(\\w+)");
        java.util.regex.Matcher anyClassMatcher = anyClassPattern.matcher(code);
        if (anyClassMatcher.find()) {
            return anyClassMatcher.group(1);
        }

        throw new IllegalArgumentException("No class found in code");
    }

    private Class<?> compileAndLoad(String className, String code) throws Exception {
        logger.info("Started compileAndLoad Method");

        File oldClassFile = new File(className + ".class");
        if (oldClassFile.exists()) {
            oldClassFile.delete();
        }

        File currentDir = new File(".");
        File[] classFiles = currentDir.listFiles((dir, name) -> name.endsWith(".class"));
        if (classFiles != null) {
            for (File f : classFiles) {
                if (!f.getName().startsWith("com.") && !f.getName().startsWith("java.")) {
                    f.delete();
                }
            }
        }

        File sourceFile = new File(className + ".java");
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(code);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new RuntimeException("Java compiler not available. Please use JDK, not JRE.");
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));

        List<String> options = Arrays.asList("-g", "-Xlint:none");

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, diagnostics, options, null, compilationUnits);

        boolean success = task.call();

        if (!success) {
            StringBuilder errors = new StringBuilder("Compilation failed:\n");
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                errors.append("Line ").append(diagnostic.getLineNumber())
                        .append(": ").append(diagnostic.getMessage(null)).append("\n");
            }
            sourceFile.delete();
            throw new RuntimeException(errors.toString());
        }

        fileManager.close();

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(".").toURI().toURL()});

        logger.info("Exiting compileAndLoad Method");

        return Class.forName(className, true, classLoader);
    }
}