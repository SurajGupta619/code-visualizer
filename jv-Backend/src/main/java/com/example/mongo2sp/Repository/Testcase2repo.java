package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.Testcase2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Testcase2repo extends MongoRepository<Testcase2,String> {
}
