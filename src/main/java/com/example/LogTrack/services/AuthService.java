package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.authDtos.LoginDto;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> studentSignUp(StudentSignUpRequest signUpRequest);

    ResponseEntity<String> supervisorSignUp(SupervisorSignUpRequest supervisorSignUpRequest);

    ResponseEntity<String> login(@Valid LoginDto loginDto);

    ResponseEntity<String> verifyAccount(String token);
}
