package com.example.mongo2sp.dto.QuestionManagementDTO;

import com.example.mongo2sp.model.QuestionType;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionManagementResDTO {
    private String id;
    private Questions questionId;
    private Topic topicId;
    private QuestionType questionTypeId;
}
