package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.logEntries.DailyEntrySummary;
import com.example.LogTrack.models.dtos.logEntries.LogEntryCreationDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryRequestDto;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.services.LogEntryService;
import com.example.LogTrack.services.WeeklySummaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController{
    private final LogEntryService logEntryService;
    private final WeeklySummaryService weeklySummaryService;

    @Autowired
    public StudentController(LogEntryService logEntryService, WeeklySummaryService weeklySummaryService) {
        this.logEntryService = logEntryService;
        this.weeklySummaryService = weeklySummaryService;
    }

    @PostMapping("/entity/create")
    public ResponseEntity<String> createLogEntry(@Valid @RequestBody LogEntryCreationDto logEntryCreationDto,
                                                 Authentication authentication){
        String email = authentication.getName();
        return logEntryService.createLogEntry(logEntryCreationDto, email);
    }

    @GetMapping("/entry/view")
    public ResponseEntity<DailyEntrySummary> viewLogEntry(@RequestParam int weekNumber,
                                                          @RequestParam int dayNo,
                                                          Authentication authentication){
        String email = authentication.getName();
        return logEntryService.viewLogEntry(weekNumber, dayNo, email);
    }

    @PatchMapping("/entry/update/{id}")
    public ResponseEntity<String> updateLogEntry(@PathVariable Long id,
                                                 @RequestBody Map<String,Object> updates,
                                                 Authentication authentication){
        String email = authentication.getName();
        return logEntryService.updateLogEntry(email, id, updates);
    }

    @DeleteMapping("/entry/delete/{id}")
    public ResponseEntity<String> deleteLogEntry(@PathVariable Long id,
                                                 Authentication authentication,
                                                 LogEntryRequestDto logEntryRequestDto){
        String email = authentication.getName();
        return logEntryService.deleteLogEntry(id, email, logEntryRequestDto);
    }

    @GetMapping("/weeklySummary")
    public ResponseEntity<WeeklySummaryViewDto> getWeeklySummary(Authentication authentication,
                                                                 @RequestParam int weekNumber){
        String email = authentication.getName();
        return weeklySummaryService.getWeeklySummary(email, weekNumber);
    }



}
