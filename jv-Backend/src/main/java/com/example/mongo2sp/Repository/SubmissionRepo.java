package com.example.mongo2sp.Repository;

import com.example.mongo2sp.Controller.SubmissionController;
import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.Submission;
import com.example.mongo2sp.model.UserCodeTracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface SubmissionRepo extends MongoRepository<Submission,String> {

    Optional<Submission> findByCodeTracker(UserCodeTracker codeTracker);
}
