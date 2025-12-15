package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Repository.RunRepo;
import com.example.mongo2sp.Services.RunService;
import com.example.mongo2sp.dto.CodeTrackerdto.CodeTrackerReqdto;
import com.example.mongo2sp.dto.RunDto.RunReqdto;
import com.example.mongo2sp.dto.RunDto.RunResdto;
import com.example.mongo2sp.model.CodeTracker;
import com.example.mongo2sp.model.Run;
import com.example.mongo2sp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/run")
public class RunController {
    @Autowired
    RunService runService;
//    @PostMapping("/insert")
//    public List<Run> insert(@RequestBody RunReqdto runReqdto){
//        return runService.insert(runReqdto);
//    }

    @GetMapping("/get")
    public List<RunResdto> get(){
        return runService.get();
    }

    @PostMapping("/data")
    public ResponseEntity<RunResdto> store(@RequestBody CodeTrackerReqdto codeTrackerReqdto){
        return ResponseEntity.status(200).body(runService.store(codeTrackerReqdto));
    }

//    @GetMapping("/find/{uid}/{qid}")
//    public CodeTracker getCode(@PathVariable String uid,@PathVariable String qid ){
//        return runService.find(uid,qid);
//    }

}
