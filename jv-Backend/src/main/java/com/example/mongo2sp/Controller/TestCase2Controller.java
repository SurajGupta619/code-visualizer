package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.Testcase2service;
import com.example.mongo2sp.model.Testcase2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/testcase")
@Controller
public class TestCase2Controller {
    @Autowired
    Testcase2service testcase2service;

    @PostMapping("/insert")
    public ResponseEntity<String> ins(@RequestBody Testcase2 testcase2){
        testcase2service.insert(testcase2);
        return ResponseEntity.ok("Value inserted");
    }

    @GetMapping("/get")
    public List<Testcase2> find(){
        return testcase2service.get();
    }
}
