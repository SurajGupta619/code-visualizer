package com.example.mongo2sp.dto.CodeSubmission;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDTO {

    private String input;
    private String output;
}
