package com.example.mongo2sp.dto.QuestionManagementDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionManagementReqDTO {
    private String questionId;
    private String topicId;
    private String questionTypeId;
}

// "topicId":"68df6335d1d809054f014856"
//         "questionTypeId":"68e887254c3c9c174cdae57d"
