package com.example.mongo2sp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Document(collection = "QuestionType")
public class QuestionType {
    @Id
    private String id;
    private String question_type;

    public QuestionType() {
    }

    public QuestionType(String question_type) {
        this.question_type = question_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }
}
