package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.authDtos.LoginDto;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import com.example.LogTrack.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp/student")
    public ResponseEntity<String> studentSignUp(@RequestBody StudentSignUpRequest signUpRequest) {
        return authService.studentSignUp(signUpRequest);
    }

    @PostMapping("/signUp/supervisor")
    public ResponseEntity<String> supervisorSignUp(@RequestBody SupervisorSignUpRequest supervisorSignUpRequest) {
        return authService.supervisorSignUp(supervisorSignUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(LoginDto)
}
