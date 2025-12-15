package com.example.mongo2sp.dto.CodeSubmission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO{
    public String user_id;
    public Integer question_type;
    public Integer topic;
    public Integer question_id;
    public String code;
    public List<Integer> ids;

}
