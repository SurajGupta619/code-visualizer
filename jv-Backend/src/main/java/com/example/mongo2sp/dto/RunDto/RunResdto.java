package com.example.mongo2sp.dto.RunDto;

import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerResdto;
import com.example.mongo2sp.model.CodeTracker;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RunResdto {
    private String id;
    private String code_tracker;
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;
}
