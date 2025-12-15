package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicRepo extends MongoRepository<Topic,String> {
}
