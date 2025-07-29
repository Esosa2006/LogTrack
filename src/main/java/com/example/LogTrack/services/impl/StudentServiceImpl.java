package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.EntryStatus;
import com.example.LogTrack.models.dtos.LogEntries.DailyEntrySummary;
import com.example.LogTrack.models.dtos.LogEntries.LogEntryCreationDto;
import com.example.LogTrack.models.dtos.LogEntries.LogEntryRequestDto;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.WeeklySummary;
import com.example.LogTrack.repositories.LogEntryRepository;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.WeeklySummaryRepository;
import com.example.LogTrack.services.StudentService;
import com.example.LogTrack.services.WeeklySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final WeeklySummaryService weeklySummaryService;
    private final WeeklySummaryRepository weeklySummaryRepository;
    private final LogEntryRepository logEntryRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, WeeklySummaryService weeklySummaryService, WeeklySummaryRepository weeklySummaryRepository, LogEntryRepository logEntryRepository) {
        this.studentRepository = studentRepository;
        this.weeklySummaryService = weeklySummaryService;
        this.weeklySummaryRepository = weeklySummaryRepository;
        this.logEntryRepository = logEntryRepository;
    }

    @Override
    public ResponseEntity<String> createLogEntry(LogEntryCreationDto logEntryCreationDto, String email) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        LogEntry logEntry = new LogEntry();
        logEntry.setDate(LocalDate.now());
        logEntry.setStudent(student);
        logEntry.setStatus(EntryStatus.PENDING);
        logEntry.setActivityDescription(logEntryCreationDto.getActivityDescription());
        studentRepository.save(weeklySummaryService.addEntryToWeeklySummary(logEntry, logEntryCreationDto.getWeekNumber(), student));
        logEntryRepository.save(logEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body("Entry successfully created");
    }

    @Override
    public ResponseEntity<DailyEntrySummary> viewLogEntry(int weekNumber, int dayNo, String email) {
        Student student = studentRepository.findByEmail(email);
        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNumber, student);
        if (weeklySummary == null) {
            throw new RuntimeException("No week with this week number was found!");
        }
        if (dayNo < 1 || dayNo > 7) {
            throw new RuntimeException("Invalid day number! Pick between days 1-6");
        }
        LogEntry logEntry = weeklySummary.getEntries().get(dayNo - 1);
        DailyEntrySummary dailyEntrySummary = new DailyEntrySummary();
        dailyEntrySummary.setDate(logEntry.getDate());
        dailyEntrySummary.setActivityDescription(logEntry.getActivityDescription());
        dailyEntrySummary.setStudentName(logEntry.getStudent().getName());
        dailyEntrySummary.setMatricNumber(logEntry.getStudent().getMatricNumber());
        dailyEntrySummary.setId(logEntry.getId());
        return ResponseEntity.status(HttpStatus.OK).body(dailyEntrySummary);
    }

    @Override
    public ResponseEntity<String> updateLogEntry(String email, Long id, Map<String, Object> updates) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            throw new RuntimeException("Student with email " + email + " not found!");
        }
        LogEntry logEntry = logEntryRepository.findByStudentAndId(student, id);
        if (logEntry == null) {
            throw new RuntimeException("Log entry for " + student.getName() + " with id " + id + " not found!");
        }
        if (logEntry.getStatus().equals(EntryStatus.PENDING)) {
            if (updates.containsKey("activityDescription")) {
                logEntry.setActivityDescription((String) updates.get("activityDescription"));
                logEntryRepository.save(logEntry);
                studentRepository.save(weeklySummaryService.addEntryToWeeklySummary(logEntry, logEntry.getWeeklySummary().getWeekNumber(), student));
                return ResponseEntity.status(HttpStatus.OK).body("Entry successfully updated");
            }
            else{
                throw new RuntimeException("You cannot edit this field!");
            }
        }
        else{
            throw new RuntimeException("You cant edit an already approved Entry!");
        }
    }

    @Override
    public ResponseEntity<String> deleteLogEntry(Long id, String email, LogEntryRequestDto logEntryRequestDto) {
        Student student = studentRepository.findByEmail(email);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found!");
        }

        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(logEntryRequestDto.getWeekNo(), student);
        if (weeklySummary == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weekly summary not found!");
        }

        int index = logEntryRequestDto.getDayNo() - 1;
        List<LogEntry> entries = weeklySummary.getEntries();
        if (index < 0 || index >= entries.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid day number!");
        }
        if (entries.size() < logEntryRequestDto.getDayNo()) {
            throw new RuntimeException("No log entry for this day.");
        }

        LogEntry logEntry = entries.get(index);
        if (logEntry == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Log entry not found!");
        }

        if (!logEntry.getStatus().equals(EntryStatus.PENDING)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only PENDING entries can be deleted.");
        }

        weeklySummary.getEntries().remove(logEntry);
        logEntry.setWeeklySummary(null);
        student.getLogEntries().remove(logEntry);
        logEntry.setStudent(null);

        logEntryRepository.delete(logEntry);
        weeklySummaryRepository.save(weeklySummary);
        studentRepository.save(student); // only if you removed logEntry from student side
        return ResponseEntity.ok("Log entry deleted successfully.");
    }



}
