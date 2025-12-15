package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.QuestionManagementService;
import com.example.mongo2sp.dto.QuestionManagementDTO.QuestionManagementReqDTO;
import com.example.mongo2sp.dto.QuestionManagementDTO.QuestionManagementResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qmanagement")
public class QuestionManagementController {

    @Autowired
    QuestionManagementService questionManagementService;

    @PostMapping(value = "/insert")
    public ResponseEntity<QuestionManagementResDTO> insert(@RequestBody QuestionManagementReqDTO questionManagementReqDTO){
        return ResponseEntity.status(201).body(questionManagementService.insert(questionManagementReqDTO));
    }

    @GetMapping("/get")
    public List<QuestionManagementResDTO> get_All(){
        return questionManagementService.getAll();
    }
}
