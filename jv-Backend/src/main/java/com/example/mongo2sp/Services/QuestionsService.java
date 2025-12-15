package com.example.mongo2sp.Services;

import com.example.mongo2sp.Repository.*;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerResdto;
import com.example.mongo2sp.dto.QuestionDTO.QuestionReqDTO;
import com.example.mongo2sp.dto.QuestionDTO.QuestionResDTO;
import com.example.mongo2sp.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.bson.types.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TopicRepo topicRepo;

    @Autowired
    QuestionTypeRepo questionTypeRepo;

    @Autowired
    UserCodeTrackerepo userCodeTrackerepo;
    @Autowired
    CodetrackerService codetrackerService;

    @Autowired
    private QuestionManageRepo questionManageRepo;
    private final CodetrackerRepo codetrackerRepo;
    private final QuestionManagementRepo questionManagementRepo;
    private final QuestionsRepo questionsRepo;


    public List<Questions> find(){
        return questionsRepo.findAll();
    }

    //    public List<QuestionResDTO> findByTopic(String tid,String qtid) {
//        List<Questions> lst = questionsRepo.findAll();
//        List<QuestionResDTO> dtoList = new ArrayList<>();
//        for(Questions i:lst) {
//            if(i.getTopic_id().getId() != null)
//            {
//            if((i.getTopic_id().getId().equals(tid)) && (i.getQuestion_type_id().getId().equals(qtid))) {
//                QuestionResDTO dto = QuestionResDTO.builder()
//                        .id(i.getId())
//                        .question_name(i.getQuestionName())
//                        .description(i.getDescription())
//                        .build();
//
//                dtoList.add(dto);
//            }
//            }
//        }
//        return dtoList;
//
//    }
    public Questions insert(QuestionReqDTO questionReqDTO){
        Questions questions = Questions.builder()
                .questionName(questionReqDTO.getQuestionName())
                .description(questionReqDTO.getDescription())
                .questionCode(questionReqDTO.getQuestionCode())
                .solutionCode(questionReqDTO.getSolutionCode())
                .build();

        return questionsRepo.save(questions);
    }

    public List<Object> getUnCompletedQuestion(Integer topicId , Integer questionTypeId ) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        List<UserCodeTracker> completedTrackers = userCodeTrackerepo.findByUserIdAndCompletionStatusTrue(user);

        List<Integer> completedIds = completedTrackers.stream()
                .map(tracker -> tracker.getQuestionId())
                .collect(Collectors.toList());

        List<QuestionManage> availableQuestions;

        if (completedIds.size()==0) {

            availableQuestions = questionManageRepo.findByTopicIdsAndQuestionTypeId(topicId, questionTypeId);

        } else {
            List<Integer> topics = new ArrayList<>();
            topics.add(topicId);
            availableQuestions = questionManageRepo.findByTopicIdsInAndQuestionTypeIdAndQuestionIdNotIn(topics, questionTypeId, completedIds);

        }
        List<Integer> questionIds = availableQuestions.stream().map(qid -> qid.getQuestionId())
                .collect(Collectors.toList());

        if(questionIds.isEmpty())
        {
            return Collections.emptyList();
        }

//        List<Questions> questions = questionsRepo.findByIdIn(questionIds);
//
//        return questions.stream().map(
//                (e)->QuestionResDTO.builder()
//                        .id(e.getId())
//                        .questionName(e.getQuestionName())
//                        .description(e.getDescription())
//                        .questionCode(e.getQuestionCode())
//                        .build()
//        ).toList();

        List<Object> questions = new ArrayList<>();
        int maxquestion_count = 3;
        int got_question = 0;

        if(completedIds.size() != 0) {
            questionIds.addAll(completedIds);
        }

        for(Integer q:questionIds){
            if(got_question==3){
                break;
            }
            QuestionManage questionManage = questionManageRepo.findByQuestionId(q).orElseThrow();
            Integer total_testcases = questionManage.getTestcaseList().size();
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setProxy(Proxy.NO_PROXY);
            RestTemplate restTemplate = new RestTemplate(factory);

            String url = "http://172.20.200.15:8001/backend/codelens/questions/?question_id="+q;
            ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);

            ObjectMapper mapper = new ObjectMapper();
            String latest_code = "";
            int passed = 0;
            try{
            UserCodeTracker codeTracker = userCodeTrackerepo.findByUserIdAndQuestionId(user,q).orElseThrow();
            latest_code = codeTracker.getLatest_code();
            passed = codeTracker.getTestCasePassed();
            }
            catch (Exception e){
                latest_code = "";
            }

            LinkedHashMap<String,Integer> testcases= new LinkedHashMap<>();
            testcases.put("TestCases",total_testcases);
            JsonNode test = mapper.valueToTree(testcases);

            ObjectNode root = mapper.createObjectNode();
            root.put("question",mapper.readTree(response.getBody()));
            root.put("testcases",total_testcases);
            root.put("passed",passed);
            root.put("latest_code",latest_code);
            got_question += 1;



            questions.add(root);

        }
        return  questions;
    }

    public List<QuestionResDTO> pickThreeQuestion(List<QuestionResDTO> list)
    {
        List<QuestionResDTO> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.size() >=3?copy.subList(0,3):copy;
    }

    private static final String API_URL = "http://172.20.200.15:8001/backend/codelens/get_questions";

    public void fetchAndStoreQuestions() {
        RestTemplate restTemplate = new RestTemplate();

        QuestionManage[] questions = restTemplate.getForObject(API_URL, QuestionManage[].class);

        if (questions != null) {
            List<QuestionManage> questionList = Arrays.asList(questions);
            questionManageRepo.saveAll(questionList);
            System.out.println(" Questions saved successfully in MongoDB!");
        } else {
            System.out.println("No data received from API!");
        }
    }

    final String TOPIC_API_URL = "http://172.20.200.15:8001/backend/codelens/topics";
    public List<List<Object>> fetchTopic() {
        RestTemplate restTemplate = new RestTemplate();
        List<List<Object>> response = restTemplate.getForObject(TOPIC_API_URL, List.class);
        System.out.println(response);
        for(List<Object> topic : response)
        {
          double res =  calculateProgress(topic.get(0));
          topic.add(res);
        }
        return response;
    }
    public double calculateProgress(Object topicId)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        List<UserCodeTracker> list = userCodeTrackerepo.findByUserIdAndTopicIdAndCompletionStatusTrue(user,(Integer) topicId);
//        System.out.println(list);
        if(list.isEmpty())
        {
            return 0.0;
        }
        double progress = (double) list.size()/9*100;
       return progress;
    }

}
