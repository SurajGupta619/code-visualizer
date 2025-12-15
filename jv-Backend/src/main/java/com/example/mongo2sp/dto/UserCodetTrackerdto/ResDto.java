package com.example.mongo2sp.dto.UserCodetTrackerdto;

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
public class ResDto {
    private String userid;
    private Integer question_id;
    private boolean completionStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime first_sub;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime last_sub;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time_taken;
    private Integer question_type_id;
    private String latest_code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latest_code_save_time;
    private Integer topic_id;
}
