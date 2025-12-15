package com.example.mongo2sp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "codetracker")
public class CodeTracker {
    @Id
    private String id;
    @DBRef
    private User userId;
    @DBRef
    private Questions questionId;

    private boolean completionStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime first_sub;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime last_sub;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time_taken;
    @DBRef
    private QuestionType question_type_id;
    private String latest_code;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime latest_code_save_time;
    
    @DBRef
    private Topic topic_id;
}
