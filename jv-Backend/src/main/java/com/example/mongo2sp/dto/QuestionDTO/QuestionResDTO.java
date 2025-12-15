package com.example.mongo2sp.dto.QuestionDTO;

import com.example.mongo2sp.dto.QuestionTypeDTO.QuestionTypeResDTO;
import com.example.mongo2sp.dto.TopicDTO.TopicResDTO;
import com.example.mongo2sp.model.QuestionType;
import com.example.mongo2sp.model.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResDTO {

    private Integer id;
    private String title;
    private String description;
    private String display_code;
//    private String solutionCode;


//    private TopicResDTO topic_id;
//    private QuestionTypeResDTO question_type_id;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getQuestionName() {
//        return questionName;
//    }
//
//    public void setQuestionName(String questionName) {
//        this.questionName = questionName;
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
//    public String getQuestionCode() {
//        return questionCode;
//    }
//
//    public void setQuestionCode(String questionCode) {
//        this.questionCode = questionCode;
//    }

//    public String getSolutionCode() {
//        return solutionCode;
//    }

//    public void setSolutionCode(String solutionCode) {
//        this.solutionCode = solutionCode;
//    }
//    public TopicResDTO getTopic_id() {
//        return topic_id;
//    }
//
//    public void setTopic_id(TopicResDTO topic_id) {
//        this.topic_id = topic_id;
//    }
//
//    public QuestionTypeResDTO getQuestion_type_id() {
//        return question_type_id;
//    }
//
//    public void setQuestion_type_id(QuestionTypeResDTO question_type_id) {
//        this.question_type_id = question_type_id;
//    }
}
