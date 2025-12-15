package com.example.mongo2sp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Name;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Document(collection = "questionmana")
public class QuestionManage {
    @Id
    private String id;
    @JsonProperty("question_id")
    private Integer questionId;

    @JsonProperty("question_type")
    private Integer questionTypeId;

    @JsonProperty("topics_id")
    private List<Integer> topicIds;

    @JsonProperty("testcase_list")
    private List<Testcase> testcaseList;

    public static class Testcase {
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    // --- Getters & Setters ---
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public List<Integer> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Integer> topicIds) {
        this.topicIds = topicIds;
    }

    public List<Testcase> getTestcaseList() {
        return testcaseList;
    }

    public void setTestcaseList(List<Testcase> testcaseList) {
        this.testcaseList = testcaseList;
    }
}
