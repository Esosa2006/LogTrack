package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
import com.example.LogTrack.models.entities.Token;
import com.example.LogTrack.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignStudentToSupervisor(@Valid @RequestBody AssignmentDto assignmentDto) {
        return adminService.assignStudentToSupervisor(assignmentDto);
    }

    @GetMapping("/supervisors")
    public List<SupervisorDto> viewAllSupervisors(){
        return adminService.viewAllSupervisors();
    }

    @GetMapping("/students")
    public List<StudentViewDto> viewAllStudents(){
        return adminService.viewAllStudents();
    }

    @GetMapping("/student")
    public List<StudentViewDto> searchForStudentsByField(@RequestBody Map<String, Object> search){
        return adminService.searchForStudentsByField(search);
    }

    @GetMapping("/supervisor")
    public List<SupervisorDto> searchForSupervisorsByField(@RequestBody Map<String, Object> search){
        return adminService.searchForSupervisorByField(search);
    }

    @GetMapping("/viewSystemDashboard")
    public ResponseEntity<String> getActiveUserCount(){
        return adminService.getActiveUserCount();
    }

    @PostMapping("/revokeToken/{id}")
    public ResponseEntity<String> revokeToken(@PathVariable Long id){
        return adminService.revokeToken(id);
    }

    @GetMapping("/tokens")
    public ResponseEntity<List<Token>> getTokens(@RequestParam String state){
        return adminService.getTokens(state);
    }

}
