package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionsRepo extends MongoRepository<Questions,String> {
//    @Query("{'topic_id':?0,'question_type_id':?1,'id':{$nin:?2}}")
//    List<Questions> findByTopicAndQuestionTypeExcludingId(String topi_id , String Question_type_id, List<String> excludingIds);
    List<Questions> findByIdIn(List<String> ids);
}
