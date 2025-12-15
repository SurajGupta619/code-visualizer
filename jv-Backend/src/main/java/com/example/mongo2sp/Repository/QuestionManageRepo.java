package com.example.mongo2sp.Repository;

import com.example.mongo2sp.model.QuestionManage;
import com.example.mongo2sp.model.QuestionManagement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionManageRepo extends MongoRepository<QuestionManage, String> {

    List<QuestionManage> findByTopicIdsInAndQuestionTypeIdAndQuestionIdNotIn(List<Integer>  topicsId, Integer questionTypeId, List<Integer> exculdedQuestionIds);
    List<QuestionManage> findByTopicIdsAndQuestionTypeId(Integer topicId, Integer questionTypeId);

    Optional<QuestionManage> findByQuestionId(Integer questionId);

    @Query(value = "{'questionId':?0}", fields = "{'testcaseList':1,'_id':0}")
    QuestionManage findTestcaseListByQuestionId(Integer questionId);
}
