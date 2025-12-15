package com.example.mongo2sp.dto.CodeSubmission;

import lombok.Data;
// Assuming you still use Lombok for brevity

@Data
public class CodeSubmission {
    // The Java code provided by the user
    private String code;

    // The input data to be fed to the code's STDIN
    private String input;

    // We can assume 'java' is the language now, but keeping it for flexibility
    private String language = "java";
}