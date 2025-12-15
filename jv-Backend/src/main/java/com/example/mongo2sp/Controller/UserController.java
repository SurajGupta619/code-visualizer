package com.example.mongo2sp.Controller;

import com.example.mongo2sp.Services.UserService;
import com.example.mongo2sp.dto.UserDto.UserResdto;
import com.example.mongo2sp.model.User;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/insert")
    public ResponseEntity<User> insert(@RequestBody User user){
        User use = userService.ins(user);
        return ResponseEntity.ok(use);
    }

    @GetMapping("/get")
    public List<UserResdto> get(){
        return userService.find();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(){
        userService.del();
        return ResponseEntity.ok("Data deleted");
    }

    @GetMapping("/findbyid")
    public UserResdto find(@RequestParam("id") String id){
        return userService.findby_id(id);
    }

    @DeleteMapping("/deletebyid")
    public ResponseEntity<List<User>> deletebyid(@RequestParam("id") String id){

        return ResponseEntity.ok(userService.delete_id(id));
    }
}
