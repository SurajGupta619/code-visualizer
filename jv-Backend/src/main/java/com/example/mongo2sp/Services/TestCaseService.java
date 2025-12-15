package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.QuestionsRepo;
import com.example.mongo2sp.Repository.QuestionsRepo;
import com.example.mongo2sp.Repository.TestCaseRepo;
import com.example.mongo2sp.dto.TestCaseDTO.TestCaseReqDTO;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestCaseService {

    @Autowired
    TestCaseRepo testCaseRepo;

    @Autowired
    QuestionsRepo questionsRepo;
    public List<TestCase> find(){
        return testCaseRepo.findAll();
    }
    public TestCase insert(String questionId , TestCase testCase){
         TestCase test = new TestCase();
         Optional<Questions> questions = questionsRepo.findById(questionId);
         if(questions.isPresent())
         {
            test.setQuestion_id(questions.get());
            test.setInput(testCase.getInput());
            test.setOutput(testCase.getOutput());
            return testCaseRepo.save(test);
         }
         return null;
    }

    public TestCase insert(TestCase testCase){
         return testCaseRepo.save(testCase);
    }

    public List<TestCase> findByquestion(String id){
        List<TestCase> testCases = testCaseRepo.findAll();
        return testCases.stream().filter((testCase -> testCase.getQuestion_id().getId().equals(id))).toList();
    }





}
