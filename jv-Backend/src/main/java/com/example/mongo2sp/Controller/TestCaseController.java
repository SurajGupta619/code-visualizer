package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.TestCaseService;
import com.example.mongo2sp.dto.TestCaseDTO.TestCaseReqDTO;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.TestCase;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/testcase")
public class TestCaseController {

    @Autowired
    TestCaseService testCaseService;

    @GetMapping("/get")
    public List<TestCase> find_all(){
        return testCaseService.find();
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insert(@RequestBody TestCase testCase) {
        testCaseService.insert(testCase.getQuestion_id().getId(),testCase);
        return ResponseEntity.ok("Value Inserted ");
    }

    @GetMapping("/findbyquestion")
    public List<TestCase> find(@RequestParam("id") String id){
        return testCaseService.findByquestion(id);
    }
}
