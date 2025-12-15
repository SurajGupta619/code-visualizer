package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.JavaVisualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.mongo2sp.model.*;

@RestController
@RequestMapping("/api/visualize")
public class VisualizationController {

    @Autowired
    private JavaVisualizationService visualizationService;

    @PostMapping("/execute")
    public VisualizationResponse executeCode(@RequestBody VisualizationRequest request) {
        return visualizationService.visualize(request);
    }
}