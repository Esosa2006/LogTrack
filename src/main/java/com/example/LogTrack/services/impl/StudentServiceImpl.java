package com.example.LogTrack.services.impl;

import com.example.LogTrack.exceptions.exceptions.StudentNotFoundException;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

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
            throw new StudentNotFoundException("Student not found");
        }
        if (updates.containsKey("name")) {
            student.setName((String) updates.get("name"));
        }
        if (updates.containsKey("email")) {
            student.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("matricNumber")) {
            student.setPassword((String) updates.get("matricNumber"));
        }
        studentRepository.save(student);
        return ResponseEntity.ok("User profile updated");
    }
}
