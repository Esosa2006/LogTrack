package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.Role;
import com.example.LogTrack.exceptions.exceptions.AuthenticationFailedException;
import com.example.LogTrack.exceptions.exceptions.EmailAlreadyInUseException;
import com.example.LogTrack.exceptions.exceptions.PasswordMissmatchException;
import com.example.LogTrack.models.dtos.authDtos.LoginDto;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.repositories.AdminRepository;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.security.JWTService;
import com.example.LogTrack.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final AdminRepository adminRepository;

    @Autowired
    public AuthServiceImpl(StudentRepository studentRepository, SupervisorRepository supervisorRepository, AuthenticationManager authenticationManager, JWTService jwtService, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
    }

    @Override
    public ResponseEntity<String> studentSignUp(StudentSignUpRequest signUpRequest) {
        Student student = studentRepository.findByEmail(signUpRequest.getEmail());
        if (student != null) {
            log.error("Email found in student Repo! Student already exists");
            throw new EmailAlreadyInUseException("This email is already registered");
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            log.error("Passwords and Confirm Password do not match!");
            throw new PasswordMissmatchException("Passwords do not match");
        }
        Student newStudent = createStudent(signUpRequest);
        studentRepository.save(newStudent);
        log.info("New Student Created");
        return ResponseEntity.status(HttpStatus.CREATED).body("Student Registration successful!");
    }

    @Override
    public ResponseEntity<String> supervisorSignUp(SupervisorSignUpRequest supervisorSignUpRequest) {
        Supervisor supervisor = supervisorRepository.findByEmail(supervisorSignUpRequest.getEmail());
        if (supervisor != null) {
            log.error("Email found in supervisor Repo! Supervisor already exists");
            throw new EmailAlreadyInUseException("This email is already registered");
        }
        if (!supervisorSignUpRequest.getPassword().equals(supervisorSignUpRequest.getConfirmPassword())) {
            log.error("Passwords do not match!");
            throw new PasswordMissmatchException("Passwords do not match");
        }
        Supervisor newSupervisor = createSupervisor(supervisorSignUpRequest);
        supervisorRepository.save(newSupervisor);
        log.info("New Supervisor Created");
        return ResponseEntity.status(HttpStatus.CREATED).body("Supervisor Registration successful!");
    }

    @Override
    public ResponseEntity<String> login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (!authentication.isAuthenticated()) {
            log.error("Invalid username or password!");
            throw new AuthenticationFailedException("Authentication failed");
        }
        log.info("Authentication successful!");
        return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(loginDto.getEmail()));
    }

    private static Student createStudent(StudentSignUpRequest studentSignUpRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Student newStudent = new Student();
        newStudent.setEmail(studentSignUpRequest.getEmail());
        newStudent.setMatricNumber(studentSignUpRequest.getMatricNumber());
        newStudent.setPassword(encoder.encode(studentSignUpRequest.getPassword()));
        newStudent.setName(studentSignUpRequest.getName());
        newStudent.setRole(Role.STUDENT);
        newStudent.setCreatedAt(new Date());
        return newStudent;
    }

    private static Supervisor createSupervisor(SupervisorSignUpRequest supervisorSignUpRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Supervisor newSupervisor = new Supervisor();
        newSupervisor.setEmail(supervisorSignUpRequest.getEmail());
        newSupervisor.setPassword(encoder.encode(supervisorSignUpRequest.getPassword()));
        newSupervisor.setName(supervisorSignUpRequest.getName());
        newSupervisor.setRole(Role.SUPERVISOR);
        newSupervisor.setCreatedAt(new Date());
        return newSupervisor;
    }
}
