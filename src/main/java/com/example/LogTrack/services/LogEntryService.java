package com.example.LogTrack.services;

import com.example.LogTrack.models.dtos.logEntries.DailyLogEntryDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryCreationDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryQueryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LogEntryService {
    ResponseEntity<String> createLogEntry(LogEntryCreationDto logEntryCreationDto, String email);

    ResponseEntity<DailyLogEntryDto> viewLogEntry(int weekNumber, int dayNo, String email);

    ResponseEntity<String> updateLogEntry(String email, Long id, Map<String, Object> updates);

    ResponseEntity<String> deleteLogEntry(Long id, String email, LogEntryQueryDto logEntryQueryDto);

    ResponseEntity<List<DailyLogEntryDto>> getByStatus(String email, String status);
}
