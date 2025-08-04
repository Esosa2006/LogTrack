package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.AssignmentDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<String> assignStudentToSupervisor(AssignmentDto assignmentDto);
}
