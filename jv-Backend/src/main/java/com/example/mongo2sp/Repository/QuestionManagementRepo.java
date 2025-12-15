package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.QuestionManagement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionManagementRepo extends MongoRepository<QuestionManagement,String> {

    List<QuestionManagement> findByTopicIdAndQuestionTypeIdAndQuestionIdNotIn(String topicId, String questionTypeId, List<String> exculdedQuestionIds);
    List<QuestionManagement> findByTopicIdAndQuestionTypeId(String topicId, String questionTypeId);
}
