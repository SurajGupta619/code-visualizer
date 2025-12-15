package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.Run;
import com.example.mongo2sp.model.UserCodeTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RunRepo extends MongoRepository<Run,String> {
    Optional<Run> findByUserCodeTracker(UserCodeTracker userCodeTracker);
}
