package com.example.mongo2sp.Services;


import com.example.mongo2sp.Repository.TopicRepo;
import com.example.mongo2sp.dto.TopicDTO.TopicReqDTO;
import com.example.mongo2sp.dto.TopicDTO.TopicResDTO;
import com.example.mongo2sp.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.SelectionOperators;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicServices {
    @Autowired
    TopicRepo topicRepo;
    public void insertTopic(List<Topic> topics)
    {
        for(Topic topic:topics){
            topicRepo.save(topic);
        }

    }

    public List<Topic> getalltopic(){
        return topicRepo.findAll();
    }



}
