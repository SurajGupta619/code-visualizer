package com.example.mongo2sp.dto.QuestionDTO;

import com.example.mongo2sp.dto.QuestionTypeDTO.QuestionTypeReqDTO;
import com.example.mongo2sp.dto.TopicDTO.TopicReqDTO;
import com.example.mongo2sp.model.QuestionType;
import com.example.mongo2sp.model.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class QuestionReqDTO {

    private String questionName;
    private String description;
    private String questionCode;
    private String solutionCode;

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getSolutionCode() {
        return solutionCode;
    }

    public void setSolutionCode(String solutionCode) {
        this.solutionCode = solutionCode;
    }
//    public String getQuestion_name() {
//        return question_name;
//    }
//
//    public void setQuestion_name(String question_name) {
//        this.question_name = question_name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public TopicReqDTO getTopic_id() {
//        return topic_id;
//    }
//
//    public void setTopic_id(TopicReqDTO topic_id) {
//        this.topic_id = topic_id;
//    }
//
//    public QuestionTypeReqDTO getQuestion_type_id() {
//        return question_type_id;
//    }
//
//    public void setQuestion_type_id(QuestionTypeReqDTO question_type_id) {
//        this.question_type_id = question_type_id;
//    }
}
