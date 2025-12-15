package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CodetrackerRepo extends MongoRepository<CodeTracker, String> {
//    Optional<List<CodeTracker>> findAllByUser_id(String writer);
//  List<CodeTracker> findByUserIdAndCompletionStatusTrue(String userId);
   List<CodeTracker> findByUserId(User user);
   List<CodeTracker> findByQuestionId(Questions questions);

   Optional<CodeTracker> findByUserIdAndQuestionId(User user,Questions questions);


   List<CodeTracker> findByUserIdAndCompletionStatusTrue(String  userid) ;
}
