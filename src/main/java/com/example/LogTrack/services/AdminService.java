package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
import com.example.LogTrack.models.entities.Token;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AdminService {
    ResponseEntity<String> assignStudentToSupervisor(AssignmentDto assignmentDto);

    List<SupervisorDto> viewAllSupervisors();

    List<StudentViewDto> viewAllStudents();

    List<StudentViewDto> searchForStudentsByField(Map<String, Object> search);

    List<SupervisorDto> searchForSupervisorByField(Map<String, Object> search);

    ResponseEntity<String> getActiveUserCount();

    ResponseEntity<String> revokeToken(Long id);

    ResponseEntity<List<Token>> getTokens(String state);
}
