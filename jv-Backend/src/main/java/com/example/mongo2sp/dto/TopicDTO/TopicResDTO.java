package com.example.mongo2sp.dto.TopicDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
public class TopicResDTO {
    private String id;
    private String topic_name;

    public TopicResDTO() {
    }

    public TopicResDTO(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }
}
