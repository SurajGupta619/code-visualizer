package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.CodetrackerRepo;
import com.example.mongo2sp.Repository.SubmissionRepo;
import com.example.mongo2sp.Repository.UserCodeTrackerepo;
import com.example.mongo2sp.dto.Submissiondto.SubmissionReqdto;
import com.example.mongo2sp.dto.Submissiondto.SubmissionResdto;
import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.Submission;
import com.example.mongo2sp.model.UserCodeTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubmissionService {
    @Autowired
    SubmissionRepo submissionRepo;
   @Autowired
   UserCodeTrackerepo userCodeTrackerepo;


//    public List<Submission> insert(SubmissionReqdto submissionReqdto){
//        CodeTracker codeTracker = codetrackerRepo.findById(submissionReqdto.getCode_tracker()).orElseThrow(()-> new RuntimeException("Data not found"));
//        Submission submission = Submission.builder().
//                codeTracker(codeTracker).
//                code(submissionReqdto.getCode()).
//                dateTime(submissionReqdto.getDateTime()).build();
//
//
//        submissionRepo.save(submission);
//        return submissionRepo.findAll();
//    }

    public List<SubmissionResdto> find(){

        List<Submission> submissions = submissionRepo.findAll();
        return submissions.stream().map(submission -> new SubmissionResdto(
                submission.getId(),
                submission.getCodeTracker().getId(),
                submission.getCode(),
                submission.getDateTime())).collect(Collectors.toList());
    }

    public Submission insertorupdate(SubmissionReqdto submissionReqdto){
        UserCodeTracker userCodeTracker = userCodeTrackerepo.findById(submissionReqdto.getCode_tracker()).orElseThrow();
        Optional<Submission> submission = submissionRepo.findByCodeTracker(userCodeTracker);
        if (submission.isPresent()){
            Submission submission1 = submission.get();
            submission1.setCode(submissionReqdto.getCode());
            submission1.setDateTime(submissionReqdto.getDateTime());
            return submissionRepo.save(submission1);
        }

        Submission submission1 = Submission.builder()
                .codeTracker(userCodeTracker)
                .code(submissionReqdto.getCode())
                .dateTime(submissionReqdto.getDateTime()).build();

        return submissionRepo.save(submission1);

    }
}
