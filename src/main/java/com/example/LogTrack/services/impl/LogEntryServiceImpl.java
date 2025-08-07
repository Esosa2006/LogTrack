package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.EntryStatus;
import com.example.LogTrack.exceptions.exceptions.*;
import com.example.LogTrack.models.dtos.logEntries.DailyLogEntryDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryCreationDto;
import com.example.LogTrack.models.dtos.logEntries.LogEntryQueryDto;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.WeeklySummary;
import com.example.LogTrack.repositories.LogEntryRepository;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.WeeklySummaryRepository;
import com.example.LogTrack.services.LogEntryService;
import com.example.LogTrack.services.WeeklySummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogEntryServiceImpl implements LogEntryService {
    private final StudentRepository studentRepository;
    private final WeeklySummaryService weeklySummaryService;
    private final WeeklySummaryRepository weeklySummaryRepository;
    private final LogEntryRepository logEntryRepository;

    @Autowired
    public LogEntryServiceImpl(StudentRepository studentRepository, WeeklySummaryService weeklySummaryService, WeeklySummaryRepository weeklySummaryRepository, LogEntryRepository logEntryRepository) {
        this.studentRepository = studentRepository;
        this.weeklySummaryService = weeklySummaryService;
        this.weeklySummaryRepository = weeklySummaryRepository;
        this.logEntryRepository = logEntryRepository;
    }

    @Override
    public ResponseEntity<String> createLogEntry(LogEntryCreationDto logEntryCreationDto, String email) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            log.error("Student with email {} not found in student Repo", email);
            throw new StudentNotFoundException("Student with email " + email + " not found!");
        }
        LogEntry logEntry = new LogEntry();
        logEntry.setDate(LocalDate.now());
        logEntry.setStatus(EntryStatus.PENDING);
        logEntry.setActivityDescription(logEntryCreationDto.getActivityDescription());
        weeklySummaryService.addEntryToWeeklySummary(logEntry, logEntryCreationDto.getWeekNumber(), student, logEntryCreationDto.getDayNo());
        log.info("Log Entry created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body("Entry successfully created");
    }

    @Override
    public ResponseEntity<DailyLogEntryDto> viewLogEntry(int weekNumber, int dayNo, String email) {
        Student student = studentRepository.findByEmail(email);
        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNumber, student);
        if (weeklySummary == null) {
            log.error("WeeklySummary not found in summary Repo");
            throw new WeeklySummaryNotFoundException("No week with this week number was found!");
        }
        if (dayNo < 1 || dayNo > 7) {
            log.error("Day number out of range");
            throw new InvalidDayNumberException("Invalid day number! Pick between days 1-6");
        }
        if(weeklySummary.getEntries().size() < dayNo){
            log.error("Day does not exist in summary");
            throw new NoLogEntryFoundException("No log entry was found!");
        }
        LogEntry logEntry = weeklySummary.getEntries().get(dayNo - 1);
        DailyLogEntryDto dailyLogEntryDto = new DailyLogEntryDto();
        dailyLogEntryDto.setDate(logEntry.getDate());
        dailyLogEntryDto.setActivityDescription(logEntry.getActivityDescription());
        dailyLogEntryDto.setComment(logEntry.getComment());
        dailyLogEntryDto.setStatus(logEntry.getStatus());
        dailyLogEntryDto.setId(logEntry.getId());
        log.info("Log Entry retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(dailyLogEntryDto);
    }

    @Override
    public ResponseEntity<String> updateLogEntry(String email, Long id, Map<String, Object> updates) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            log.error("Student with email {} not found in student repository", email);
            throw new StudentNotFoundException("Student with email " + email + " not found!");
        }
        LogEntry logEntry = logEntryRepository.findById(id).orElseThrow(() -> new NoLogEntryFoundException("Log entry for " + student.getName() + " with id " + id + " not found!"));
        if (logEntry.getStatus().equals(EntryStatus.PENDING)) {
            if (updates.containsKey("activityDescription")) {
                logEntry.setActivityDescription((String) updates.get("activityDescription"));
                logEntryRepository.save(logEntry);
                studentRepository.save(weeklySummaryService.addEntryToWeeklySummary(logEntry, logEntry.getWeeklySummary().getWeekNumber(), student));
                log.info("Log Entry updated successfully");
                return ResponseEntity.status(HttpStatus.OK).body("Entry successfully updated");
            }
            else{
                log.error("Failed to update log entry.");
                throw new FieldRestrictionException("You cannot edit this field!");
            }
        }
        else{
            log.error("Log entry with id {} has already been approved by supervisor", id);
            throw new EntryAlreadyApprovedException("You cant edit an already approved Entry!");
        }
    }

    @Override
    public ResponseEntity<String> deleteLogEntry(Long id, String email, LogEntryQueryDto logEntryQueryDto) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            log.error("Student with email {} not found in student repo", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found!");
        }

        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(logEntryQueryDto.getWeekNo(), student);
        if (weeklySummary == null) {
            log.error("Weekly Summary not found in summary Repo");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly summary not found!");
        }

        int index = logEntryQueryDto.getDayNo() - 1;
        List<LogEntry> entries = weeklySummary.getEntries();
        if (index < 0 || index >= entries.size()) {
            log.error("Day no out of range");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid day number!");
        }
        if (entries.size() < logEntryQueryDto.getDayNo()) {
            log.error("Weekly summary does not have day {}", logEntryQueryDto.getDayNo());
            throw new NoLogEntryFoundException("No log entry for this day.");
        }

        LogEntry logEntry = entries.get(index);
        if (logEntry == null) {
            log.error("Log entry not found in week {} summary", logEntryQueryDto.getWeekNo());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Log entry not found!");
        }

        if (!logEntry.getStatus().equals(EntryStatus.PENDING)) {
            log.error("Entry status of log entry is not pending!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only PENDING entries can be deleted.");
        }
        logEntryRepository.delete(logEntry);
        log.info("Log entry deleted successfully");
        return ResponseEntity.ok("Log entry deleted successfully.");
    }

    @Override
    public ResponseEntity<List<DailyLogEntryDto>> getByStatus(String email, String status) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            log.info("Student with email ({}) not found in student repo", email);
            throw new StudentNotFoundException("Student with email " + email + " not found!");
        }
        EntryStatus entryStatus = EntryStatus.valueOf(status);
        List<LogEntry> logEntryList = logEntryRepository.findByStatusAndStudentId(entryStatus, student.getId());
        List<DailyLogEntryDto> dailyLogEntryDtoList = new ArrayList<>();
        for (LogEntry logEntry : logEntryList){
            DailyLogEntryDto dailyLogEntryDto = new DailyLogEntryDto();
            dailyLogEntryDto.setDate(logEntry.getDate());
            dailyLogEntryDto.setActivityDescription(logEntry.getActivityDescription());
            dailyLogEntryDto.setComment(logEntry.getComment());
            dailyLogEntryDto.setStatus(logEntry.getStatus());
            dailyLogEntryDto.setId(logEntry.getId());
            dailyLogEntryDtoList.add(dailyLogEntryDto);
        }
        log.info("Entries retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(dailyLogEntryDtoList);
    }

}
