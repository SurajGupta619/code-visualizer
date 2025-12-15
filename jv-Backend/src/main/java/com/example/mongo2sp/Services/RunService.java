package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.*;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerReqdto;
import com.example.mongo2sp.dto.RunDto.RunReqdto;
import com.example.mongo2sp.dto.RunDto.RunResdto;
import com.example.mongo2sp.dto.UserDto.UserResdto;
import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.Run;
import com.example.mongo2sp.model.User;
import com.example.mongo2sp.model.UserCodeTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RunService {
    @Autowired
    RunRepo runRepo;
    @Autowired
    QuestionTypeRepo questionTypeRepo;
    @Autowired
    TopicRepo topicRepo;
    @Autowired
    QuestionsRepo questionsRepo;
    @Autowired
    UserCodeTrackerepo userCodeTrackerepo;

    @Autowired
    UserRepository userRepository;
//    public List<Run> insert(RunReqdto runReqdto){
//        CodeTracker codeTracker = codetrackerRepo.findById(runReqdto.getCode_tracker()).orElseThrow();
//        Run run = Run.builder()
//                .codeTracker(codeTracker)
//                .code(runReqdto.getCode())
//                .dateTime(runReqdto.getDateTime())
//        .build();
//        return runRepo.findAll();
//    }

//    public CodeTracker find(String userid,String questionid){
//        Optional<CodeTracker> ct = codetrackerRepo.findByUserIdAndQuestionId(userid,questionid);
//        if (ct.isPresent()){
//            return ct.get();
//        }
//        return null;
//    }
//
    public RunResdto store(CodeTrackerReqdto codeTrackerReqdto){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

       // User user = userRepository.findById(userid).get();


        Optional<UserCodeTracker> ct = userCodeTrackerepo.findByUserIdAndQuestionId(user,codeTrackerReqdto.getQuestion_id());
        if (ct.isPresent()){
            UserCodeTracker userCodeTracker = ct.get();
            userCodeTracker.setId(ct.get().getId());
            userCodeTracker.setLatest_code(codeTrackerReqdto.getCode());
            userCodeTracker.setLatest_code_save_time(LocalDateTime.now());

            UserCodeTracker userCodeTracker1 = userCodeTrackerepo.save(userCodeTracker);

            Optional<Run> run = runRepo.findByUserCodeTracker(userCodeTracker1);
            if (run.isPresent()){
                Run run1 = run.get();
                run1.setId(run.get().getId());
                run1.setCode(userCodeTracker1.getLatest_code());
                run1.setDateTime(LocalDateTime.now());
                Run run2 = runRepo.save(run1);
                return RunResdto.builder()
                        .id(run2.getId())
                        .code_tracker(run2.getUserCodeTracker().getId())
                        .code(run2.getCode())
                        .dateTime(run2.getDateTime())
                        .build();
            }

        }

        UserCodeTracker userCodeTracker = new UserCodeTracker();
        userCodeTracker.setUserId(userRepository.findById(user.getId()).orElseThrow());
        userCodeTracker.setQuestionId(codeTrackerReqdto.getQuestion_id());
        userCodeTracker.setCompletionStatus(codeTrackerReqdto.isCompletion_status());
        userCodeTracker.setQuestion_type_id(codeTrackerReqdto.getQuestion_type_id());
        userCodeTracker.setLatest_code(codeTrackerReqdto.getCode());
        userCodeTracker.setLatest_code_save_time(LocalDateTime.now());
        userCodeTracker.setTopicId(codeTrackerReqdto.getTopic_id());

        UserCodeTracker userCodeTracker1 = userCodeTrackerepo.save(userCodeTracker);

        Run run = new Run();
        run.setUserCodeTracker(userCodeTracker1);
        run.setCode(userCodeTracker1.getLatest_code());
        run.setDateTime(LocalDateTime.now());

        Run r1 = runRepo.save(run);
        return RunResdto.builder()
                .id(r1.getId())
                .code_tracker(r1.getUserCodeTracker().getId())
                .code(r1.getCode())
                .dateTime(r1.getDateTime())
                .build();
    }

    public List<RunResdto> get(){


        List<Run> runs =  runRepo.findAll();
        return runs.stream().map(run -> new RunResdto(
                run.getId(),
                run.getUserCodeTracker().getId(),
                run.getCode(),
                run.getDateTime())).collect(Collectors.toList());
    }
}
