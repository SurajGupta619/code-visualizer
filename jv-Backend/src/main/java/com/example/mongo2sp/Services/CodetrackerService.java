package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.*;
import com.example.mongo2sp.Repository.*;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerReqdto;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerResdto;
import com.example.mongo2sp.dto.QuestionDTO.QuestionResDTO;
import com.example.mongo2sp.dto.UserCodetTrackerdto.ReqDto;
import com.example.mongo2sp.dto.UserCodetTrackerdto.ResDto;
import com.example.mongo2sp.dto.UserDto.UserResdto;
import com.example.mongo2sp.model.*;
import com.example.mongo2sp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CodetrackerService {
    @Autowired
    UserCodeTrackerepo userCodeTrackerepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionTypeRepo questionTypeRepo;

    @Autowired
    TopicRepo topicRepo;

    @Autowired
    QuestionsRepo questionsRepo;



//    public CodeTracker insert(CodeTrackerReqdto codeTrackerReqdto){
//        User user = userRepository.findById(codeTrackerReqdto.getUser_id()).orElseThrow();
//
//        Questions questions = questionsRepo.findById(codeTrackerReqdto.getQuestion_id()).orElseThrow();
//
//        QuestionType type = questionTypeRepo.findById(codeTrackerReqdto.getQuestion_type_id()).orElseThrow();
//
//        Topic topic = topicRepo.findById(codeTrackerReqdto.getTopic_id()).orElseThrow();
//
//        CodeTracker codeTracker = CodeTracker.builder()
//                .userId(user)
//                .questionId(questions)
//                .completionStatus(codeTrackerReqdto.isCompletion_status())
//                .first_sub(codeTrackerReqdto.getFirst_sub())
//                .last_sub(codeTrackerReqdto.getLast_sub())
//                .time_taken(codeTrackerReqdto.getTime_taken())
//                .question_type_id(type)
//                .latest_code(codeTrackerReqdto.getLatest_code())
//                .latest_code_save_time(codeTrackerReqdto.getLatest_code_save_time())
//                .topic_id(topic)
//                .build();
//
//        return codetrackerRepo.save(codeTracker);
//
//    }

//    public List<CodeTrackerResdto> get(){
//
//        List<CodeTracker> codeTrackers = codetrackerRepo.findAll();
//
//        return codeTrackers.stream().map(codeTracker ->{
//            User user = codeTracker.getUserId();
//            UserResdto userResdto = new UserResdto(user.getId(),user.getUsername(), user.getName());
//
//            Questions questions = codeTracker.getQuestionId();
//            Topic topic = codeTracker.getTopic_id();
//            QuestionType questionType = codeTracker.getQuestion_type_id();
//
//            return CodeTrackerResdto.builder().id(codeTracker.getId())
//                    .userid(user.getId())
//                    .question_id(questions.getId())
//                    .first_sub(codeTracker.getFirst_sub())
//                    .last_sub(codeTracker.getLast_sub())
//                    .completionStatus(codeTracker.isCompletionStatus())
//                    .time_taken(codeTracker.getTime_taken())
//                    .latest_code(codeTracker.getLatest_code())
//                    .latest_code_save_time(codeTracker.getLatest_code_save_time())
//                    .question_type_id(questionType.getId())
//                    .topic_id(topic.getId()).build();
//        }).collect(Collectors.toList());
//
//    }

//    public List<String> findQuestionIdByUserid(String userid)
//    {
//        List<String> questionid = new ArrayList();
//        List<CodeTracker>  list = codetrackerRepo.findAll();
//        for(CodeTracker track : list)
//        {
//            User use = track.getUserId();
//            String r = use.getId();
//            if(userid.equals(r))
//            {
//                questionid.add(track.getQuestionId().getId());
//            }
//
//        }
//        return questionid;
//    }


        public List<UserCodeTracker> getUser(String userid){
            return userCodeTrackerepo.findByUserId(userRepository.findById(userid).orElseThrow());

        }

//        public List<UserCodeTracker> getquestion(String questionid){
//        return codetrackerRepo.findByQuestionId(questionsRepo.findById(questionid).orElseThrow());
//        }


//        public Optional<CodeTracker> getquestionanduser(String userid,Integer questionid){
//        return codetrackerRepo.findByUserIdAndQuestionId(userRepository.findById(userid).orElseThrow(),questionsRepo.findById(questionid).orElseThrow());
//        }
//===========================================================================================================
        public UserCodeTracker insertorupdate(ReqDto codeTrackerReqdto){
        Optional<UserCodeTracker> codeTracker = userCodeTrackerepo.findByUserIdAndQuestionId(
                userRepository.findById(codeTrackerReqdto.getUser_id()).orElseThrow(),
                codeTrackerReqdto.getQuestion_id());

        if(codeTracker.isPresent()){

            UserCodeTracker codeTracker1 = codeTracker.get();
                if (codeTracker1.getFirst_sub() == null){
                    codeTracker1.setFirst_sub(codeTrackerReqdto.getFirst_sub());
                }

                codeTracker1.setLatest_code(codeTrackerReqdto.getLatest_code());
                codeTracker1.setLast_sub(codeTrackerReqdto.getLast_sub());
                codeTracker1.setCompletionStatus(codeTrackerReqdto.isCompletion_status());
                codeTracker1.setTestCasePassed(codeTrackerReqdto.getTestCasePassed());
                return userCodeTrackerepo.save(codeTracker1);
        }

        User user = userRepository.findById(codeTrackerReqdto.getUser_id()).orElseThrow();

            UserCodeTracker codeTracker1 = UserCodeTracker.builder()
                        .userId(user)
                        .questionId(codeTrackerReqdto.getQuestion_id())
                        .completionStatus(codeTrackerReqdto.isCompletion_status())
                        .first_sub(codeTrackerReqdto.getFirst_sub())
                        .last_sub(codeTrackerReqdto.getLast_sub())
                        .time_taken(codeTrackerReqdto.getTime_taken())
                        .question_type_id(codeTrackerReqdto.getQuestion_type_id())
                        .latest_code(codeTrackerReqdto.getLatest_code())
                        .latest_code_save_time(codeTrackerReqdto.getLatest_code_save_time())
                        .topicId(codeTrackerReqdto.getTopic_id())
                        .testCasePassed(codeTrackerReqdto.getTestCasePassed())
                        .build();
        return userCodeTrackerepo.save(codeTracker1);
        }

    public ResDto ins(ReqDto reqDto){
        UserCodeTracker userCodeTracker = UserCodeTracker.builder()
                .userId(userRepository.findById(reqDto.getUser_id()).orElseThrow())
                .questionId(reqDto.getQuestion_id())
                .completionStatus(reqDto.isCompletion_status())
                .first_sub(reqDto.getFirst_sub())
                .last_sub(reqDto.getLast_sub())
                .time_taken(reqDto.getTime_taken())
                .question_type_id(reqDto.getQuestion_type_id())
                .latest_code(reqDto.getLatest_code())
                .latest_code_save_time(reqDto.getLatest_code_save_time())
                .topicId(reqDto.getTopic_id()).build();
        UserCodeTracker userCodeTracker1 = userCodeTrackerepo.save(userCodeTracker);
        return ResDto.builder().userid(userCodeTracker1.getUserId().getId())
                .question_id(userCodeTracker1.getQuestionId())
                .completionStatus(userCodeTracker1.isCompletionStatus())
                .first_sub(userCodeTracker1.getFirst_sub())
                .last_sub(userCodeTracker1.getLast_sub())
                .time_taken(userCodeTracker1.getTime_taken())
                .question_type_id(userCodeTracker1.getQuestion_type_id())
                .latest_code(userCodeTracker1.getLatest_code())
                .latest_code_save_time(userCodeTracker1.getLatest_code_save_time())
                .topic_id(userCodeTracker1.getTopicId()).build();
    }

    public List<ResDto> fin(){
        List<UserCodeTracker> codeTrackers = userCodeTrackerepo.findAll();
        return codeTrackers.stream().map((userCodeTracker -> ResDto.builder()
                .userid(userCodeTracker.getUserId().getId())
                .question_id(userCodeTracker.getQuestionId())
                .completionStatus(userCodeTracker.isCompletionStatus())
                .first_sub(userCodeTracker.getFirst_sub())
                .last_sub(userCodeTracker.getLast_sub())
                .time_taken(userCodeTracker.getTime_taken())
                .question_type_id(userCodeTracker.getQuestion_type_id())
                .latest_code(userCodeTracker.getLatest_code())
                .latest_code_save_time(userCodeTracker.getLatest_code_save_time())
                .topic_id(userCodeTracker.getTopicId()).build())).toList();
    }
    public UserCodeTracker insert(UserCodeTracker userCodeTracker)
    {
        User use = userRepository.findById(userCodeTracker.getUserId().getId()).orElseThrow();
        UserCodeTracker code = new UserCodeTracker();
//        code.set
        return userCodeTrackerepo.save(userCodeTracker);
    }
}
