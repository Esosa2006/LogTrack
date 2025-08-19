package com.example.LogTrack.controllers;

import com.example.LogTrack.models.dtos.logEntries.DailyLogEntryDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryCreationDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryQueryDto;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.services.LogEntryService;
import com.example.LogTrack.services.WeeklySummaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class LogEntryController {
    private final LogEntryService logEntryService;
    private final WeeklySummaryService weeklySummaryService;

    @Autowired
    public LogEntryController(LogEntryService logEntryService, WeeklySummaryService weeklySummaryService) {
        this.logEntryService = logEntryService;
        this.weeklySummaryService = weeklySummaryService;
    }

//    @PostMapping("/entry/create")
//    public ResponseEntity<String> createLogEntry(@Valid @RequestBody LogEntryCreationDto logEntryCreationDto,
//                                                 Authentication authentication){
//        String email = authentication.getName();
//        return logEntryService.createLogEntry(logEntryCreationDto, email);
//    }

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
                                                 LogEntryQueryDto logEntryQueryDto){
        String email = authentication.getName();
        return logEntryService.deleteLogEntry(id, email, logEntryQueryDto);
    }

    @GetMapping("/weeklySummary")
    public ResponseEntity<WeeklySummaryViewDto> getWeeklySummary(Authentication authentication,
                                                                 @RequestParam int weekNumber){
        String email = authentication.getName();
        return weeklySummaryService.getWeeklySummary(email, weekNumber);
    }

    @GetMapping("/entries")
    public ResponseEntity<List<DailyLogEntryDto>> getByStatus(Authentication authentication,
                                                              @RequestParam String status){
        String email = authentication.getName();
        return logEntryService.getByStatus(email, status);
    }

    @GetMapping("/byDay")
    public ResponseEntity<DailyLogEntryDto> viewLogEntry(@RequestParam String dayOfTheWeek,
                                                     @RequestParam int weekNumber,
                                                     Authentication authentication){
        String email = authentication.getName();
        return logEntryService.viewLogEntry(email, dayOfTheWeek, weekNumber);
    }

    @PostMapping("/entry/createByDay")
    public ResponseEntity<String> createByDay(@RequestBody LogEntryCreationDto logEntryCreationDto,
                                              Authentication authentication){
        String email = authentication.getName();
        return logEntryService.createLogEntryWithDay(logEntryCreationDto, email);
    }
}
