package com.example.mongo2sp.Services;


import com.example.mongo2sp.Repository.TestCaseTrackerRepository;
import com.example.mongo2sp.model.Submission;
import com.example.mongo2sp.model.TestCaseTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseTrackerService {

    @Autowired
    TestCaseTrackerRepository testCaseTrackerRepository;


    public List<TestCaseTracker> findAll(){
        return testCaseTrackerRepository.findAll();
    }

    public void insert(TestCaseTracker testCaseTracker){
        testCaseTrackerRepository.save(testCaseTracker);
    }

}
