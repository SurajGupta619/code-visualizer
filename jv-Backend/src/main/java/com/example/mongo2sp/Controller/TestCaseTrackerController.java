package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.TestCaseTrackerService;
import com.example.mongo2sp.model.Submission;
import com.example.mongo2sp.model.TestCaseTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test-case-tracker")
public class TestCaseTrackerController {

    @Autowired
    TestCaseTrackerService testCaseTrackerService;


    @GetMapping("/get")
    public List<TestCaseTracker> find_all(){
        return testCaseTrackerService.findAll();
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insert(@RequestBody TestCaseTracker testCaseTracker){
        testCaseTrackerService.insert(testCaseTracker);
        return ResponseEntity.ok("Value Inserted ");
    }


}
