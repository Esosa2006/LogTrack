package com.example.LogTrack.services.impl;

import com.example.LogTrack.exceptions.exceptions.StudentNotFoundException;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public ResponseEntity<String> updateProfile(String email, Map<String, Object> updates) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            log.error("Student with email {} not found in student Repo", email);
            throw new StudentNotFoundException("Student not found");
        }
        if (updates.containsKey("name")) {
            student.setName((String) updates.get("name"));
            log.info("Student name successfully updated to {}", updates.get("name"));
        }
        if (updates.containsKey("email")) {
            student.setEmail((String) updates.get("email"));
            log.info("Student email successfully updated to {}",  updates.get("email"));
        }
        if (updates.containsKey("matricNumber")) {
            student.setPassword((String) updates.get("matricNumber"));
            log.info("Student matric number successfully updated to {}", updates.get("matricNumber"));
        }
        studentRepository.save(student);
        log.info("User profile updated and saved!");
        return ResponseEntity.ok("User profile updated");
    }
}
