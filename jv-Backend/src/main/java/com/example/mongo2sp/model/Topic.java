package com.example.mongo2sp.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Document(collection = "Topic")
public class Topic {
    @Id
    private String id;
    private String title;
    private String desc;
    private String progress;

}
