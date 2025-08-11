package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.EvaluationDto;
import com.example.LogTrack.models.dtos.SupervisorAssignedStudentDto;
import com.example.LogTrack.models.dtos.logEntries.SupervisorLogEntryView;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SupervisorService {
    ResponseEntity<List<SupervisorLogEntryView>> viewStudentLogEntries(String email, String matricNo);

    ResponseEntity<String> evaluateEntry(int weekNo, int dayNo, String matricNo, String email, EvaluationDto dto);

    ResponseEntity<WeeklySummaryViewDto> viewStudentWeeklySummary(String email, String matricNo, int weekNo);

    ResponseEntity<List<SupervisorAssignedStudentDto>> viewAssignedStudents(String email);
}
