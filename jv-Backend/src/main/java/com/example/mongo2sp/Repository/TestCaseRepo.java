package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.Submission;
import com.example.mongo2sp.model.TestCase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepo extends MongoRepository<TestCase,String> {

}
