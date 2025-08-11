package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
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
}
