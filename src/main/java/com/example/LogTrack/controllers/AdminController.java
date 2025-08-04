package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public ResponseEntity<String> assignStudentToSupervisor(@Valid @RequestBody AssignmentDto assignmentDto) {
        return adminService.assignStudentToSupervisor(assignmentDto);
    }
}
