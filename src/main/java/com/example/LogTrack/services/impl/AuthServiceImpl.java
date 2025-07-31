package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.Role;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
import com.example.LogTrack.models.dtos.authDtos.SupervisorSignUpRequest;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;

    @Autowired
    public AuthServiceImpl(StudentRepository studentRepository, SupervisorRepository supervisorRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public ResponseEntity<String> studentSignUp(StudentSignUpRequest signUpRequest) {
        Student student = studentRepository.findByEmail(signUpRequest.getEmail());
        if (student != null) {
            throw new RuntimeException("This email is already registered");
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        Student newStudent = createStudent(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Student Registration successful!");
    }

    @Override
    public ResponseEntity<String> supervisorSignUp(SupervisorSignUpRequest supervisorSignUpRequest) {
        Supervisor supervisor = supervisorRepository.findByEmail(supervisorSignUpRequest.getEmail());
        if (supervisor != null) {
            throw new RuntimeException("This email is already registered");
        }
        Supervisor newSupervisor = createSupervisor(supervisorSignUpRequest);
        supervisorRepository.save(newSupervisor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Supervisor Registration successful!");
    }

    private static Student createStudent(StudentSignUpRequest studentSignUpRequest) {
        if (!studentSignUpRequest.getPassword().equals(studentSignUpRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Student newStudent = new Student();
        newStudent.setEmail(studentSignUpRequest.getEmail());
        newStudent.setMatricNumber(studentSignUpRequest.getMatricNumber());
        newStudent.setPassword(encoder.encode(studentSignUpRequest.getPassword()));
        newStudent.setName(studentSignUpRequest.getName());
        newStudent.setRole(Role.STUDENT);
        return newStudent;
    }

    private static Supervisor createSupervisor(SupervisorSignUpRequest supervisorSignUpRequest) {
        if (!supervisorSignUpRequest.getPassword().equals(supervisorSignUpRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Supervisor newSupervisor = new Supervisor();
        newSupervisor.setEmail(supervisorSignUpRequest.getEmail());
        newSupervisor.setPassword(encoder.encode(supervisorSignUpRequest.getPassword()));
        newSupervisor.setName(supervisorSignUpRequest.getName());
        newSupervisor.setRole(Role.SUPERVISOR);
        return newSupervisor;
    }
}
