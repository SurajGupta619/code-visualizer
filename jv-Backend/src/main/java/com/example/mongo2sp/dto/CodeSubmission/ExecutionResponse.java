package com.example.mongo2sp.dto.CodeSubmission;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutionResponse {
    // The standard output (STDOUT) captured from the execution
    private String output;

    // The error message (compilation or runtime error)
    private String error;

    // Status to indicate success or type of failure
    private String status; // e.g., "SUCCESS", "COMPILE_ERROR", "RUNTIME_ERROR"
}
