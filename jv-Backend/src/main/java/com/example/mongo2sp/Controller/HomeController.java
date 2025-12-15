package com.example.mongo2sp.Controller;


import com.example.mongo2sp.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HomeController {
    @GetMapping("/home")
    public ResponseEntity<?> home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the protected home page!");
        response.put("user", username);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/me2")
    public String me2() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
//        User = UserRepository.// subject / username
        return "Hello, " + username;
    }

}
