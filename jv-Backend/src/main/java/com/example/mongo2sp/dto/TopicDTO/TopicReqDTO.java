package com.example.mongo2sp.dto.TopicDTO;

import lombok.Builder;

@Builder
public class TopicReqDTO {
   private String topic_name;

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }
}
