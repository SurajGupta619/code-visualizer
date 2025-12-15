package com.example.mongo2sp.dto.CodeTrackerdto;

import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeTrackerReqdto {
    private String user_id;
    private Integer question_id;
    private boolean completion_status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime first_sub;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime last_sub;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time_taken;
    private Integer question_type_id;
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latest_code_save_time;
    private Integer topic_id;
}
