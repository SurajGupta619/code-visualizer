package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Repository.QuestionManageRepo;
import com.example.mongo2sp.Repository.UserRepository;
import com.example.mongo2sp.Services.*;
import com.example.mongo2sp.dto.CodeSubmission.SubmissionDTO;
import com.example.mongo2sp.dto.CodeSubmission.TestCaseDTO;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerReqdto;
import com.example.mongo2sp.dto.Submissiondto.SubmissionReqdto;
import com.example.mongo2sp.dto.UserCodetTrackerdto.ReqDto;
import com.example.mongo2sp.model.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/code")
public class CodeJudgeController {

    private final CodeExecutionService codeExecutionService;
    @Autowired
    CodetrackerController codetrackerController;
    @Autowired
    SubmissionService submissionService;
    @Autowired
    CodetrackerService codetrackerService;

    @Autowired
    TestCaseService testCaseService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionManageRepo questionManageRepo;

    public static void delete_files(){
        Path dir = Paths.get("C:\\Users\\2927140\\Desktop\\springboot\\mongo2sp\\temp_user_code");

        try {
            Files.walk(dir) // Traverse the file tree in depth-first order
                    .sorted(Comparator.reverseOrder()) // Sort in reverse order to delete contents before the directory
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Error deleting " + path + ": " + e.getMessage());
                        }
                    });

        }
        catch (IOException e) {
            System.err.println("Error walking the file tree: " + e.getMessage());
        }
    }


    @Autowired
    public CodeJudgeController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }
    public static class CodeSubmission {
        public String code;
        public String input;
        //public String expectedOutput = "0";
    }

    @PostMapping("/execute")
    public Map<String, Object> submitCode(@RequestBody CodeSubmission submission) {



        List<String> compilationErrors =
                codeExecutionService.compileCode(submission.code);
        if (!compilationErrors.isEmpty()) {
            return Map.of(
                    "status", "COMPILATION_ERROR",
                    "errors", compilationErrors);
        }
        String actualOutput =
                codeExecutionService.executeCode(submission.input);
        // String normalizedExpected = submission.expectedOutput.trim();
        String normalizedActual = actualOutput.trim();
        if (actualOutput.startsWith("EXECUTION ERROR") ||
                actualOutput.startsWith("RUNTIME ERROR")) {
            return Map.of(
                    "status", "RUNTIME_ERROR",
                    "errors", actualOutput
            );
        }
       delete_files();
        return Map.of(
                "status", "SUCCESSFUL",
                "input", submission.input,
                "output", actualOutput
        );
    }

 //   @Data
//    public static class SubmissionDTO{
//        public String user_id;
//        public Integer question_type;
//        public Integer topic;
//        public Integer question_id;
//        public String code;
//        public List<Integer> ids;
//
//    }

//    @PostMapping("/submit")
//    public Map<String,Object> submission(@RequestBody SubmissionDTO submissionDTO) {
//        boolean completionStatus = false;
//        Integer question_id = submissionDTO.question_id;
//        String code = submissionDTO.code;
//        List<TestCase> testCases = testCaseService.findByquestion(question_id);
//        System.out.println(testCases);
//        int Passed = 0;
//        int Failed = 0;
//        for (int i = 0; i < testCases.size(); i++) {
//            List<String> compilationErrors =
//                    codeExecutionService.compileCode(code);
//            if (!compilationErrors.isEmpty()) {
//                return Map.of(
//                        "status", "COMPILATION_ERROR",
//                        "errors", compilationErrors
//                );
//            }
//            String actualOutput = codeExecutionService.executeCode(testCases.get(i).getInput());
//            String normalizedExpected = testCases.get(i).getOutput().trim();
//            String normalizedActual = actualOutput.trim();
//            if (actualOutput.startsWith("EXECUTION ERROR") ||
//                    actualOutput.startsWith("RUNTIME ERROR")) {
//                return Map.of(
//                        "status", "RUNTIME_ERROR",
//                        "errors", actualOutput
//                );
//            }
//            boolean passed = normalizedActual.equals(normalizedExpected);
//            if(passed){
//                Passed++;
//            }
//            else{
//                Failed++;
//            }
//        }
//        if(Passed == testCases.size()){
//            completionStatus = true;
//        }
//        LocalDateTime submissiontime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
//
//        delete_files();
//        ReqDto codeTrackerReqdto = ReqDto.builder()
//                .user_id(submissionDTO.user_id)
//                .question_id(submissionDTO.question_id)
//                .completion_status(completionStatus)
//                .first_sub(submissiontime)
//                .last_sub(submissiontime)
//                .latest_code(code)
//                .latest_code_save_time(submissiontime)
//                .question_type_id(submissionDTO.question_type)
//                .topic_id(submissionDTO.topic).build();
//
//        UserCodeTracker codeTracker = codetrackerService.insertorupdate(codeTrackerReqdto);
//
//        SubmissionReqdto submissionReqdto = SubmissionReqdto.builder()
//                .code_tracker(codeTracker.getId())
//                .code(codeTracker.getLatest_code())
//                .dateTime(submissiontime).build();
//
//
//        submissionService.insertorupdate(submissionReqdto);
//
//
//        return Map.of(
//                    "total",testCases.size(),
//                "question_id",question_id,
//                "passed",Passed,
//                "failed",Failed,
//                "completion_status",completionStatus,
//                "time",submissiontime);
//    }
//

    private static final String API_URL = "http://172.20.200.15:8001/backend/codelens/testCases/?testcase_id=";
    @PostMapping("/test")
    public List<?> test(@RequestBody SubmissionDTO submissionDTO)
    {
        RestTemplate restTemplate = new RestTemplate();

        List<Integer> ids = submissionDTO.getIds();
        String s = "";
        for(int i = 0;i<ids.size();i++){
            if(i==(ids.size()-1)){
                s+=ids.get(i)+"";
            }else {
                s+=ids.get(i)+",";
            }
        }

        TestCaseDTO[] testCaseDTOS = restTemplate.getForObject((API_URL+s), TestCaseDTO[].class);

        List<TestCaseDTO> testCases = Arrays.asList(testCaseDTOS);
        return testCases;

    }
    @PostMapping("/submission")
    public Map<String,Object> submit(@RequestBody SubmissionDTO submissionDTO){

        RestTemplate restTemplate = new RestTemplate();
        Integer questionid = submissionDTO.getQuestion_id();
        List<Integer> ids = getTestcase(questionid);
        String s = "";
        for(int i = 0;i<ids.size();i++){
            if(i==(ids.size()-1)){
                s+=ids.get(i)+"";
            }else {
                s+=ids.get(i)+",";
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();


        TestCaseDTO[] testCaseDTOS = restTemplate.getForObject((API_URL+s), TestCaseDTO[].class);
         System.out.println(testCaseDTOS);

        List<TestCaseDTO> testCases = Arrays.asList(testCaseDTOS);
        System.out.println(testCases);

        Integer question_id = submissionDTO.getQuestion_id();
        String code = submissionDTO.getCode();
        boolean completionStatus = false;

        System.out.println(testCases);
        int Passed = 0;
        int Failed = 0;
        for (int i = 0; i < testCases.size(); i++) {
            List<String> compilationErrors =
                    codeExecutionService.compileCode(code);
            if (!compilationErrors.isEmpty()) {
                return Map.of(
                        "status", "COMPILATION_ERROR",
                        "errors", compilationErrors
                );
            }
            String actualOutput = codeExecutionService.executeCode(testCases.get(i).getInput());
            String normalizedExpected = testCases.get(i).getOutput().trim();
            String normalizedActual = actualOutput.trim();
            if (actualOutput.startsWith("EXECUTION ERROR") ||
                    actualOutput.startsWith("RUNTIME ERROR")) {
                return Map.of(
                        "status", "RUNTIME_ERROR",
                        "errors", actualOutput
                );
            }
            boolean passed = normalizedActual.equals(normalizedExpected);
            if(passed){
                Passed++;
            }
            else{
                Failed++;
            }
        }
        if(Passed == testCases.size()){
            completionStatus = true;
        }
        LocalDateTime submissiontime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        delete_files();
        ReqDto codeTrackerReqdto = ReqDto.builder()
                .user_id(user.getId())
                .question_id(submissionDTO.getQuestion_id())
                .completion_status(completionStatus)
                .first_sub(submissiontime)
                .last_sub(submissiontime)
                .latest_code(code)
                .latest_code_save_time(submissiontime)
                .question_type_id(submissionDTO.getQuestion_type())
                .topic_id(submissionDTO.getTopic())
                .testCasePassed(Passed)
                .build();


        UserCodeTracker codeTracker = codetrackerService.insertorupdate(codeTrackerReqdto);

        SubmissionReqdto submissionReqdto = SubmissionReqdto.builder()
                .code_tracker(codeTracker.getId())
                .code(codeTracker.getLatest_code())
                .dateTime(submissiontime).build();


        submissionService.insertorupdate(submissionReqdto);


        return Map.of(
                "total",testCases.size(),
                "question_id",question_id,
                "passed",Passed,
                "failed",Failed,
                "completion_status",completionStatus,
                "time",submissiontime);
    }

   @GetMapping("/testcase/{questionId}")

    public List<Integer> getTestcase(@PathVariable Integer questionId){
        QuestionManage question = questionManageRepo.findTestcaseListByQuestionId(questionId);
        if(question == null || question.getTestcaseList() == null)
        {
            return List.of();
        }
        return question.getTestcaseList()
                .stream()
                .map(QuestionManage.Testcase::getId)
                .collect(Collectors.toList());
    }
}