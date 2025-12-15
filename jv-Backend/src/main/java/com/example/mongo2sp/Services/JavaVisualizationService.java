package com.example.mongo2sp.Services;

import com.example.mongo2sp.model.*;
import com.example.mongo2sp.model.VisualizationResponse.*;
import com.example.mongo2sp.model.VisualizationRequest;
import com.example.mongo2sp.executor.HybridExecutor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class JavaVisualizationService {

    public VisualizationResponse visualize(VisualizationRequest request) {
        System.out.println("start of visualize");
        InputData inputData = new InputData(
                request.getCode(),
                request.getInput() != null ? request.getInput() : "",
                request.getBreakLineNumbers() != null ? request.getBreakLineNumbers() : new ArrayList<>()
        );

        try {
            HybridExecutor executor = new HybridExecutor(request.getCode(), request.getInput());
            List<ExecutionFrame> frames = executor.execute();

            System.out.println("Inside visualize... Total frames : " + frames.size());
            System.out.println("end of visualize");

            return new VisualizationResponse(inputData, frames);
        } catch (Exception e) {
            e.printStackTrace();
            ExecutionFrame errorFrame = new ExecutionFrame(
                    "error",
                    e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName(),
                    "",
                    1,
                    "0",
                    "0",
                    new ArrayList<>(),
                    new HashMap<>(),
                    ""
            );
            System.out.println("end of visualize, In Catch Exception : " + e.toString());
            return new VisualizationResponse(inputData, Collections.singletonList(errorFrame));
        }
    }
}