package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Repository.QuestionManageRepo;
import com.example.mongo2sp.Repository.UserRepository;
import com.example.mongo2sp.Services.CodetrackerService;
import com.example.mongo2sp.Services.QuestionManagementService;
import com.example.mongo2sp.Services.QuestionsService;
import com.example.mongo2sp.Services.TestCaseService;
import com.example.mongo2sp.dto.QuestionDTO.QuestionReqDTO;
import com.example.mongo2sp.dto.QuestionDTO.QuestionResDTO;
import com.example.mongo2sp.dto.QuestionManagementDTO.QuestionManagementReqDTO;
import com.example.mongo2sp.dto.QuestionManagementDTO.QuestionManagementResDTO;
import com.example.mongo2sp.dto.TestCaseDTO.TestCaseReqDTO;
import com.example.mongo2sp.dto.UserCodetTrackerdto.ReqDto;
import com.example.mongo2sp.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    QuestionsService questionsService;
    @Autowired
    CodetrackerService codetrackerService;
    @Autowired
    TestCaseService testCaseService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionManageRepo questionManageRepo;
    @Autowired
    QuestionManagementService questionManagementService;
    @GetMapping("/get")
    public List<Questions> find_all(){
        return questionsService.find();
    }

    @PostMapping("/insert")
    public ResponseEntity<String>  insert(@RequestBody QuestionReqDTO questionReqDTO , @RequestBody QuestionManagementReqDTO questionManagementReqDTO , @RequestBody TestCase testCase){

        Questions question = questionsService.insert(questionReqDTO);
        String questionid = question.getId();
        QuestionManagementResDTO questionManagementResDTO = questionManagementService.insert(questionid,questionManagementReqDTO);
        TestCase testCases = testCaseService.insert(questionid, testCase);
        return ResponseEntity.status(201).body("Question,questionmanagement and Testcases for that is created");
    }
    @PostMapping("/insertingQuestion")
    public Questions insert(@RequestBody QuestionReqDTO questionReqDTO)
    {
        return questionsService.insert(questionReqDTO);
    }
    @GetMapping("/allquestion")
    public List<Questions> allquestion()
    {
        return questionsService.find();
    }

    @PostMapping("/uncomplete")
    public ResponseEntity<List<Object>> getUncompletedQuestions(@RequestBody dto dto1) throws JsonProcessingException {
        System.out.println(dto1.questionTypeId);
        System.out.println(dto1.topicId);
        List<Object> questionsrem = questionsService.getUnCompletedQuestion(dto1.topicId,dto1.questionTypeId);
        return ResponseEntity.status(200).body(questionsrem);
    }

    @GetMapping("/fetch-and-store")
    public String fetchAndStoreQuestions() {
        questionsService.fetchAndStoreQuestions();
        return "Data fetched from API and stored in MongoDB successfully!";
    }

    static class dto{

        public Integer topicId;
        public Integer questionTypeId;

    }
    @GetMapping("/getquestionmanage")
    public List<QuestionManage> fetchquestionmanage()
    {
        return questionManageRepo.findAll();
    }

    @GetMapping("/check")
    public List<QuestionManage> check(@RequestBody dto dto1){
        return questionManageRepo.findByTopicIdsAndQuestionTypeId(dto1.topicId,dto1.questionTypeId);
    }
    @GetMapping("/fetchTopic")
    public List<List<Object>> fetchtopic()
    {
        return questionsService.fetchTopic();
    }
    @PostMapping("/insertUsercode")
    public UserCodeTracker insert(UserCodeTracker userCodeTracker)
    {
        return codetrackerService.insert(userCodeTracker);
    }

    @PostMapping("/uct/storeCode")
    public ResponseEntity<UserCodeTracker> insertUpdateCode(@RequestBody ReqDto reqDto)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        reqDto.setUser_id(user.getId());
        try{
            return ResponseEntity.status(200).body(codetrackerService.insertorupdate(reqDto));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(401).build();
        }
    }

}
