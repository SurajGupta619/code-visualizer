package com.example.mongo2sp.dto.QuestionTypeDTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@Data
public class QuestionTypeReqDTO {
    private String question_type;

    public QuestionTypeReqDTO() {
    }

    public QuestionTypeReqDTO(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }
}
