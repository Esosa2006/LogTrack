package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> studentSignUp(StudentSignUpRequest signUpRequest);

    ResponseEntity<String> supervisorSignUp(SupervisorSignUpRequest supervisorSignUpRequest);
}
