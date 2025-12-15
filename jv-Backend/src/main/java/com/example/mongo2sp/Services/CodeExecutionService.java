package com.example.mongo2sp.Services;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.github.javaparser.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Service;
@Service
public class CodeExecutionService {

    private static final String WORKSPACE_DIR = "temp_user_code/";
    public static String findMainClass(String code) {

        CompilationUnit cu = StaticJavaParser.parse(code);

        for (ClassOrInterfaceDeclaration cls : cu.findAll(ClassOrInterfaceDeclaration.class)) {

            for (MethodDeclaration method : cls.getMethods()) {
                if (isMainMethod(method)) {
                    return cls.getNameAsString();
                }
            }
        }
        return null;
    }

    private static boolean isMainMethod(MethodDeclaration method) {
        if (!method.getNameAsString().equals("main")) return false;
        if (!method.isPublic() || !method.isStatic()) return false;
        if (!method.getType().isVoidType()) return false;

        if (method.getParameters().size() != 1) return false;
        Parameter param = method.getParameter(0);
        return param.getType().asString().matches("String\\[\\]\\s*\\w*");
    }
    String CLASS_NAME;

    public List<String> compileCode(String code) {
        CLASS_NAME = findMainClass(code);
        System.out.println("Main class: " + CLASS_NAME);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(CLASS_NAME == null){
            return List.of("ERROR: Couldn't find an valid class name");
        }
        String FILE_NAME = CLASS_NAME + ".java";
        if (compiler == null) {

            return List.of("ERROR: JavaCompiler not available. Run application with a JDK.");
        }


        Path workspacePath = Path.of(WORKSPACE_DIR);
        try {
            Files.createDirectories(workspacePath);
            Files.list(workspacePath).forEach(path -> {
                try { Files.deleteIfExists(path); } catch (IOException ignored) {}
            });
        } catch (IOException e) {
            return List.of("ERROR: Could not set up workspace directory: " + e.getMessage());
        }

        File sourceFile = new File(WORKSPACE_DIR, FILE_NAME);

        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(code);
        } catch (IOException e) {
            return List.of("ERROR: Could not save source file: " + e.getMessage());
        }


        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));

        List<String> options = Arrays.asList("-d", WORKSPACE_DIR);

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                diagnostics,
                options,
                null,
                compilationUnits
        );

        boolean success = task.call();

        if (!success) {
            return diagnostics.getDiagnostics().stream()
                    .map(d -> formatDiagnostic(d))
                    .toList();
        }


        return List.of();
    }

    private String formatDiagnostic(Diagnostic<? extends JavaFileObject> d) {
        return "Line " + d.getLineNumber() +
                ", Column " + d.getColumnNumber() +
                ": " + d.getMessage(null);
    }


    public String executeCode(String input) {

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", WORKSPACE_DIR, CLASS_NAME
            );


            Process process = pb.start();
            process.getOutputStream().write(input.getBytes());
            process.getOutputStream().close();

            boolean finished = process.waitFor(20, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                return "EXECUTION ERROR: Timeout (20 seconds)";
            }


            String output = new String(process.getInputStream().readAllBytes());


            String errorOutput = new String(process.getErrorStream().readAllBytes());

            if (process.exitValue() != 0 || !errorOutput.isBlank()) {
                return "RUNTIME ERROR:\n" + errorOutput;
            }

            return output.trim();

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "EXECUTION ERROR: Internal server error: " + e.getMessage();
        }
    }

}