package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.DailyEntrySummary;
import com.example.LogTrack.models.dtos.LogEntryCreationDto;
import com.example.LogTrack.models.dtos.LogEntryRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface StudentService {
    ResponseEntity<String> createLogEntry(LogEntryCreationDto logEntryCreationDto, String email);

    ResponseEntity<DailyEntrySummary> viewLogEntry(int weekNumber, int dayNo, String email);

    ResponseEntity<String> updateLogEntry(String email, Long id, Map<String, Object> updates);

    ResponseEntity<String> deleteLogEntry(Long id, String email, LogEntryRequestDto logEntryRequestDto);
}
