package com.example.mongo2sp.model;

import lombok.Data;
import java.util.List;

@Data
public class VisualizationRequest {
    private String code;
    private String input;
    private List<Integer> breakLineNumbers;
}
