package com.example.mongo2sp.dto.CodeTrackerdto;

import com.example.mongo2sp.dto.UserDto.UserResdto;
import com.example.mongo2sp.model.QuestionType;
import com.example.mongo2sp.model.Questions;
import com.example.mongo2sp.model.Topic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@CrossOrigin()
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTrackerResdto {
    private String id;
    private String userid;
    private String question_id;
    private boolean completionStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime first_sub;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime last_sub;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time_taken;
    private String question_type_id;
    private String latest_code;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latest_code_save_time;
    private String topic_id;
}
