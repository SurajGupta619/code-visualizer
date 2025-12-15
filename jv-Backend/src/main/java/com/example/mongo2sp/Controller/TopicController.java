package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Repository.TopicRepo;
import com.example.mongo2sp.Services.TopicServices;
import com.example.mongo2sp.dto.TopicDTO.TopicReqDTO;
import com.example.mongo2sp.dto.TopicDTO.TopicResDTO;
import com.example.mongo2sp.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topic")
public class TopicController {
    @Autowired
    TopicServices topicServices;
    @PostMapping("/topicinsert")
    public ResponseEntity<String> insert(@RequestBody List<Topic> topics)
    {
        topicServices.insertTopic(topics);
        return ResponseEntity.ok("Values inserted");
    }
    @GetMapping("/get")
    public List<Topic> get(){
        return topicServices.getalltopic();
    }


}
