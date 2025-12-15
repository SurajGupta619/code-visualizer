package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.SubmissionService;
import com.example.mongo2sp.dto.Submissiondto.SubmissionReqdto;
import com.example.mongo2sp.dto.Submissiondto.SubmissionResdto;
import com.example.mongo2sp.model.Submission;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/submission")
public class SubmissionController {
    @Autowired
    SubmissionService submissionService;


//    @PostMapping("/insert")
//    public List<Submission> insert(@RequestBody SubmissionReqdto submissionReqdto){
//        return submissionService.insert(submissionReqdto);
//
//    }
    @GetMapping("/get")
    public List<SubmissionResdto> get(){
        return submissionService.find();
    }
}
