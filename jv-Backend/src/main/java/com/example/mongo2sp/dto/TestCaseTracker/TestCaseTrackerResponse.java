package com.example.mongo2sp.dto.TestCaseTracker;

import com.example.mongo2sp.model.Submission;
import com.example.mongo2sp.model.TestCaseTracker;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;


@Data
public class TestCaseTrackerResponse {

    private String id;

    private String submission_id;

    private String testCase_id;

    private boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
