package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.CodetrackerService;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerReqdto;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerResdto;
import com.example.mongo2sp.dto.UserCodetTrackerdto.ReqDto;
import com.example.mongo2sp.dto.UserCodetTrackerdto.ResDto;
import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.UserCodeTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/codetracker")
public class CodetrackerController {

    @Autowired
    CodetrackerService codetrackerService;
//    @PostMapping("/insert")
//    public ResponseEntity<CodeTracker> insert(@RequestBody CodeTrackerReqdto codeTrackerReqdto){
//        return ResponseEntity.status(201).body(codetrackerService.insert(codeTrackerReqdto));
//    }
//    @GetMapping("/get")
//    public List<CodeTrackerResdto> find(){
//        return codetrackerService.get();
//    }

   @GetMapping("/getuser")
    public List<UserCodeTracker> get(@RequestParam("userid") String userid){
        return codetrackerService.getUser(userid);
   }

//   @GetMapping("/getquestion")
//    public List<UserCodeTracker> getquestion(@RequestParam("id") String id){
//        return codetrackerService.getquestion(id);
//   }

//   @GetMapping("/getuseques")
//    public Optional<CodeTracker> getques(@RequestParam("user") String uid, @RequestParam("que") Integer qid){
//        return codetrackerService.getquestionanduser(uid,qid);
//   }

   @PostMapping("/uct/insert")
    public ResDto ins(@RequestBody ReqDto reqDto){
        return codetrackerService.ins(reqDto);
   }

   @GetMapping("/uct/get")
    public List<ResDto> fin(){
        return codetrackerService.fin();
   }
}
