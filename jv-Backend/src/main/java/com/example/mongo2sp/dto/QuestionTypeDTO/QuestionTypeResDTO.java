package com.example.mongo2sp.dto.QuestionTypeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@Data
@AllArgsConstructor
public class QuestionTypeResDTO {
    private String id;
    private String question_type;

    public QuestionTypeResDTO() {
    }

    public QuestionTypeResDTO(String question_type) {
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
