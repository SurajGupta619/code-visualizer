package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.User;
import com.example.mongo2sp.model.UserCodeTracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCodeTrackerepo extends MongoRepository<UserCodeTracker,String> {
//    List<UserCodeTracker> findByUserIdAndCompletionStatusTrue(User userId);
   List<UserCodeTracker> findByUserIdAndCompletionStatusTrue(User userId);
   List<UserCodeTracker> findByUserId(User userId);
   Optional<UserCodeTracker> findByUserIdAndQuestionId(User userId, Integer questionId);

   List<UserCodeTracker> findByUserIdAndTopicIdAndCompletionStatusTrue(User userId,Integer topicId);

}
