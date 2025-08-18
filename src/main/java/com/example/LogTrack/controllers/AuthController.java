package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.authDtos.LoginDto;
import com.example.LogTrack.models.dtos.authDtos.ResetPasswordDto;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import com.example.LogTrack.services.AuthService;
import com.example.LogTrack.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp/student")
    public ResponseEntity<String> studentSignUp(@Valid @RequestBody StudentSignUpRequest signUpRequest) {
        return authService.studentSignUp(signUpRequest);
    }

    @PostMapping("/signUp/supervisor")
    public ResponseEntity<String> supervisorSignUp(@Valid @RequestBody SupervisorSignUpRequest supervisorSignUpRequest) {
        return authService.supervisorSignUp(supervisorSignUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestParam String token){
        return authService.verifyAccount(token);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email){
        return authService.resetPassword(email);
    }

    @PostMapping("/verifyResetPasswordToken")
    public ResponseEntity<String> verifyResetPasswordToken(@RequestParam String token,
                                                           @Valid @RequestBody ResetPasswordDto resetPasswordDto){
        return authService.verifyResetPasswordToken(token, resetPasswordDto);
    }
}

