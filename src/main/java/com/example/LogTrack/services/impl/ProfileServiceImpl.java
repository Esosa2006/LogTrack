package com.example.LogTrack.services.impl;

import com.example.LogTrack.exceptions.exceptions.StudentNotFoundException;
import com.example.LogTrack.exceptions.exceptions.SupervisorNotFoundException;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.services.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    private final SupervisorRepository supervisorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ProfileServiceImpl(SupervisorRepository supervisorRepository, StudentRepository studentRepository) {
        this.supervisorRepository = supervisorRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ResponseEntity<String> updateStudentProfile(String email, Map<String, Object> updates) {
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

    @Override
    public ResponseEntity<String> updateSupervisorProfile(String email, Map<String, Object> updates) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            log.error("Supervisor with email {} not found in supervisor Repository", email);
            throw new SupervisorNotFoundException("Supervisor not found");
        }
        if (updates.containsKey("name")){
            supervisor.setName((String) updates.get("name"));
            log.info("Name updated successfully to {}",  updates.get("name"));
        }
        if (updates.containsKey("email")){
            supervisor.setEmail((String) updates.get("email"));
            log.info("Email updated successfully to {}",  updates.get("email"));
        }
        supervisorRepository.save(supervisor);
        log.info("User profile updated successfully");
        return ResponseEntity.ok("Successfully updated");
    }
}
