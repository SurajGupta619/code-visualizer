package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.TestCaseTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestCaseTrackerRepository extends MongoRepository<TestCaseTracker, String> {

}
