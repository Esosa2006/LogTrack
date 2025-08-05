package com.example.LogTrack.controllers;

import com.example.LogTrack.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateProfile(Authentication authentication,
                                                @RequestBody Map<String,Object> updates){
        String email = authentication.getName();
        return studentService.updateProfile(email, updates);
    }
}
