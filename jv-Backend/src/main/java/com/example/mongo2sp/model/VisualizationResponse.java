package com.example.mongo2sp.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisualizationResponse {
    private InputData in;
    private List<ExecutionFrame> out;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InputData {
        private String code;
        private String input;
        private List<Integer> breakLineNumbers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExecutionFrame {
        private String event;
        private String errorMessage;
        private String currentObjectName;
        private int stepNo;
        private String lastLineNo;
        private String nextLineNo;
        private List<FrameInfo> frameList;
        private Map<String, HeapObject> heap;
        private String printedString;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FrameInfo {
        private String objectName;
        private int lineno;
        private String className;
        private Map<String, Object[]> locals;
        private Object[] _return;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeapObject {
        private String type;
        private Integer length;
        private List<Object[]> elements;
        private Map<String, Object[]> fields;
    }
}