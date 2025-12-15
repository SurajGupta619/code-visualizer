package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.Testcase2repo;
import com.example.mongo2sp.model.Testcase2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Testcase2service {
    @Autowired
    Testcase2repo testcase2repo;
    public void insert(Testcase2 testcase2){
        testcase2repo.save(testcase2);
    }

    public List<Testcase2> get(){
        return testcase2repo.findAll();
    }
}
