
package com.example.mongo2sp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "QuestionManagement")
public class QuestionManagement{
    @Id
    private String id;

    @DBRef
    private Questions questionId;

    @DBRef
    private Topic topicId;
    @DBRef
    private QuestionType questionTypeId;

//    private List<String> testcaseList;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Questions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Questions questionId) {
        questionId = questionId;
    }

    public Topic getTopicId() {
        return this.topicId;
    }

    public void setTopic_id(Topic topic_id) {
        this.topicId = topicId;
    }

    public QuestionType getQuestion_type_id() {
        return questionTypeId;
    }

    public void setQuestion_type_id(QuestionType question_type_id) {
        this.questionTypeId = question_type_id;
    }
}
