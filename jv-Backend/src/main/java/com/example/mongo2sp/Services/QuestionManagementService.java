package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.QuestionManagementRepo;
import com.example.mongo2sp.Repository.QuestionTypeRepo;
import com.example.mongo2sp.Repository.QuestionsRepo;
import com.example.mongo2sp.Repository.TopicRepo;
import com.example.mongo2sp.dto.QuestionManagementDTO.QuestionManagementReqDTO;
import com.example.mongo2sp.dto.QuestionManagementDTO.QuestionManagementResDTO;
import com.example.mongo2sp.model.QuestionManagement;
import com.example.mongo2sp.model.QuestionType;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionManagementService {

    @Autowired
    QuestionManagementRepo questionManagementRepo;

    @Autowired
    QuestionsRepo questionsRepo;

    @Autowired
    TopicRepo topicRepo;

    @Autowired
    QuestionTypeRepo questionTypeRepo;

    public QuestionManagementResDTO insert(QuestionManagementReqDTO questionManagementReqDTO){
        Questions questions=questionsRepo.findById(questionManagementReqDTO.getQuestionId()).orElseThrow();
        Topic topic=topicRepo.findById(questionManagementReqDTO.getTopicId()).orElseThrow();
        QuestionType questionType=questionTypeRepo.findById(questionManagementReqDTO.getQuestionTypeId()).orElseThrow();

        QuestionManagement questionManagement=QuestionManagement.builder()
                .questionId(questions)
                .topicId(topic)
                .questionTypeId(questionType)
                .build();
        QuestionManagement saved=questionManagementRepo.save(questionManagement);
        return QuestionManagementResDTO.builder()
                .id(saved.getId())
                .questionId(saved.getQuestionId())
                .topicId(saved.getTopicId())
                .questionTypeId(saved.getQuestionTypeId())
                .build();
    }
    public QuestionManagementResDTO insert(String questionid,QuestionManagementReqDTO questionManagementReqDTO){
        Questions questions=questionsRepo.findById(questionid).orElseThrow();
        Topic topic=topicRepo.findById(questionManagementReqDTO.getTopicId()).orElseThrow();
        QuestionType questionType=questionTypeRepo.findById(questionManagementReqDTO.getQuestionTypeId()).orElseThrow();

        QuestionManagement questionManagement=QuestionManagement.builder()
                .questionId(questions)
                .topicId(topic)
                .questionTypeId(questionType)
                .build();
        QuestionManagement saved=questionManagementRepo.save(questionManagement);
        return QuestionManagementResDTO.builder()
                .id(saved.getId())
                .questionId(saved.getQuestionId())
                .topicId(saved.getTopicId())
                .questionTypeId(saved.getQuestionTypeId())
                .build();
    }

    public List<QuestionManagementResDTO> getAll(){
        return questionManagementRepo.findAll().stream().map(
                      (i)->QuestionManagementResDTO.builder()
                              .id(i.getId())
                              .questionId(i.getQuestionId())
                              .topicId(i.getTopicId())
                              .questionTypeId(i.getQuestionTypeId())
                              .build()
                          ).toList();
    }
}
