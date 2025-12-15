package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.QuestionTypeServices;
import com.example.mongo2sp.dto.QuestionTypeDTO.QuestionTypeReqDTO;
import com.example.mongo2sp.dto.QuestionTypeDTO.QuestionTypeResDTO;
import com.example.mongo2sp.model.QuestionType;
import com.example.mongo2sp.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questiontype")
public class QuestionTypeController {
    @Autowired
    QuestionTypeServices questiontypeservices;
    @PostMapping("/Questioninsert")
    public ResponseEntity<QuestionTypeResDTO> insert(@RequestBody QuestionTypeReqDTO questionTypeReqDTO)
    {
        QuestionTypeResDTO q =questiontypeservices.insert(questionTypeReqDTO);
        return ResponseEntity.status(201).body(q);
    }

    @GetMapping("/getQuestionType")
    public ResponseEntity<List<QuestionTypeResDTO>> getallquestiontype()
    {
        List<QuestionTypeResDTO> result = questiontypeservices.getallquestion();
        return ResponseEntity.status(200).body(result);
    }
}
