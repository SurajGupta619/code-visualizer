package com.example.mongo2sp.dto.TestCaseDTO;

import com.example.mongo2sp.dto.QuestionDTO.QuestionReqDTO;
import com.example.mongo2sp.model.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseReqDTO {

    private String question_id;
    private String input;
    private String output;

//    public QuestionReqDTO getQuestion_id() {
//        return question_id;
//    }
//
//    public void setQuestion_id(QuestionReqDTO question_id) {
//        this.question_id = question_id;
//    }
//
//    public String getInput() {
//        return input;
//    }
//
//    public void setInput(String input) {
//        this.input = input;
//    }
//
//    public String getOutput() {
//        return output;
//    }
//
//    public void setOutput(String output) {
//        this.output = output;
//    }
}
