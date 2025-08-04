package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
import com.example.LogTrack.models.entities.Supervisor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<String> assignStudentToSupervisor(AssignmentDto assignmentDto);

    List<SupervisorDto> viewAllSupervisors();

    List<StudentViewDto> viewAllStudents();
}
