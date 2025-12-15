package com.example.mongo2sp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "question")
@Builder
public class Questions {
    @Id
    private String id;

    private String questionName;

    private String description;

    private String questionCode;

    private String solutionCode;
//    @DBRef
//    private Topic topic_id;
//    @DBRef
//    private QuestionType question_type_id;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
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
//    public Topic getTopic_id() {
//        return topic_id;
//    }
//
//    public void setTopic_id(Topic topic_id) {
//        this.topic_id = topic_id;
//    }
//
//    public QuestionType getQuestion_type_id() {
//        return question_type_id;
//    }
//
//    public void setQuestion_type_id(QuestionType question_type_id) {
//        this.question_type_id = question_type_id;
//    }

}
