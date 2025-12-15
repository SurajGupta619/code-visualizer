package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.QuestionType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionTypeRepo extends MongoRepository<QuestionType,String> {
}
