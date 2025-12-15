package com.example.mongo2sp.dto.TestCaseTracker;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestCaseTrackerRequest {

    private String id;

    private String submission_id;

    private String testCase_id;

    private boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
