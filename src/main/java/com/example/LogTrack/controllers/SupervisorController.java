package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.EvaluationDto;
import com.example.LogTrack.models.dtos.SupervisorAssignedStudentDto;
import com.example.LogTrack.models.dtos.logEntries.SupervisorLogEntryView;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {
    private final SupervisorService supervisorService;

    @Autowired
    public SupervisorController(SupervisorService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @GetMapping("/viewStudentEntry")
    public ResponseEntity<List<SupervisorLogEntryView>> viewStudentLogEntries(Authentication authentication,
                                                                              @RequestParam String matricNo){
        String email = authentication.getName();
        return supervisorService.viewStudentLogEntries(email, matricNo);
    }

    @PostMapping("/evaluateEntry")
    public ResponseEntity<String> evaluateEntry(@RequestParam int weekNo,
                                                @RequestParam int dayNo,
                                                @RequestParam String matricNo,
                                                @RequestBody EvaluationDto dto,
                                                Authentication authentication){
        String email = authentication.getName();
        return supervisorService.evaluateEntry(weekNo, dayNo, matricNo, email, dto);
    }

    @GetMapping("/viewWeekSummary")
    public ResponseEntity<WeeklySummaryViewDto> viewStudentWeeklySummary(Authentication authentication,
                                                                         @RequestParam String matricNo,
                                                                         @RequestParam int weekNo){
        String email = authentication.getName();
        return supervisorService.viewStudentWeeklySummary(email, matricNo, weekNo);
    }

    @GetMapping("/students")
    public ResponseEntity<List<SupervisorAssignedStudentDto>> viewAssignedStudents(Authentication authentication){
        String email = authentication.getName();
        return supervisorService.viewAssignedStudents(email);
    }


}
