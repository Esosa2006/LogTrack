package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.EntryStatus;
import com.example.LogTrack.exceptions.exceptions.StudentNotFoundException;
import com.example.LogTrack.exceptions.exceptions.SupervisorNotFoundException;
import com.example.LogTrack.exceptions.exceptions.WeeklySummaryNotFoundException;
import com.example.LogTrack.mapper.LogEntryForSupervisorMapper;
import com.example.LogTrack.mapper.StudentToSupervisorViewMapper;
import com.example.LogTrack.mapper.SummaryDisplayMapper;
import com.example.LogTrack.models.dtos.EvaluationDto;
import com.example.LogTrack.models.dtos.SupervisorAssignedStudentDto;
import com.example.LogTrack.models.dtos.logEntries.SupervisorLogEntryView;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.models.entities.WeeklySummary;
import com.example.LogTrack.repositories.LogEntryRepository;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.repositories.WeeklySummaryRepository;
import com.example.LogTrack.services.SupervisorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SupervisorServiceImpl implements SupervisorService {
    private final SupervisorRepository  supervisorRepository;
    private final StudentRepository studentRepository;
    private final LogEntryForSupervisorMapper  logEntryForSupervisorMapper;
    private final WeeklySummaryRepository weeklySummaryRepository;
    private final LogEntryRepository logEntryRepository;
    private final SummaryDisplayMapper summaryDisplayMapper;
    private final StudentToSupervisorViewMapper studentToSupervisorViewMapper;

    @Autowired
    public SupervisorServiceImpl(SupervisorRepository supervisorRepository, StudentRepository studentRepository, LogEntryForSupervisorMapper logEntryForSupervisorMapper, WeeklySummaryRepository weeklySummaryRepository, LogEntryRepository logEntryRepository, SummaryDisplayMapper summaryDisplayMapper, StudentToSupervisorViewMapper studentToSupervisorViewMapper) {
        this.supervisorRepository = supervisorRepository;
        this.studentRepository = studentRepository;
        this.logEntryForSupervisorMapper = logEntryForSupervisorMapper;
        this.weeklySummaryRepository = weeklySummaryRepository;
        this.logEntryRepository = logEntryRepository;
        this.summaryDisplayMapper = summaryDisplayMapper;
        this.studentToSupervisorViewMapper = studentToSupervisorViewMapper;
    }

    @Override
    public ResponseEntity<List<SupervisorLogEntryView>> viewStudentLogEntries(String email, String matricNo) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            log.error("Supervisor with email {} not found in supervisor repo", email);
            throw new RuntimeException("Supervisor not found");
        }
        Student student = studentRepository.findByMatricNumber(matricNo);
        if (student == null) {
            log.error("Student with email {} not found in student repo", email);
            throw new RuntimeException("Student with MatricNumber " + matricNo + " not found");
        }

        if (supervisor.getStudents().contains(student)) {
            List<SupervisorLogEntryView> logEntryViewList = new ArrayList<>();
            for (WeeklySummary weeklySummary : student.getWeeklySummaries()){
                for (LogEntry entry : weeklySummary.getEntries()){
                    logEntryViewList.add(logEntryForSupervisorMapper.toLogEntryView(entry, weeklySummary));
                }
            }
            log.info("Log entries retrieved successfully");
            return ResponseEntity.ok(logEntryViewList);
        }
        log.info("Student with email {} not found in supervisor's list of assigned students", email);
        throw new RuntimeException("This student is not assigned to you!");
    }

    @Override
    public ResponseEntity<String> evaluateEntry(int weekNo, int dayNo, String matricNo, String email, EvaluationDto dto) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            log.error("Supervisor with email ({}) not found in supervisor repository", email);
            throw new RuntimeException("Supervisor not found");
        }
        Student student = studentRepository.findByMatricNumber(matricNo);
        if (student == null) {
            log.error("Student with matric Number ({}) not found in student Repository", matricNo);
            throw new RuntimeException("Student with MatricNumber " + matricNo + " not found");
        }
        if (supervisor.getStudents().contains(student)) {
            WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNo, student);
            if (weeklySummary == null) {
                log.error("Weekly summary not found!");
                throw new RuntimeException("Weekly summary not found");
            }
            List<LogEntry> entries = weeklySummary.getEntries();
            if (entries == null || entries.isEmpty() || dayNo < 1 || dayNo > entries.size()) {
                log.error("Day number out of range or entries list is empty");
                throw new RuntimeException("Invalid day number: " + dayNo);
            }
            LogEntry logEntry = entries.get(dayNo - 1);
            if (logEntry == null) {
                log.error("Log entry for day {} not found", dayNo);
                throw new RuntimeException("Log entry for day " + dayNo + " not found");
            }
            if (dto.getStatus().equalsIgnoreCase("Approved")){
                logEntry.setStatus(EntryStatus.APPROVED);
                log.info("Status changed to approved!");
            }
            else if (dto.getStatus().equalsIgnoreCase("Rejected")){
                logEntry.setStatus(EntryStatus.REJECTED);
                log.info("Status changed to rejected!");
            }
            else{
                logEntry.setStatus(EntryStatus.PENDING);
            }
            logEntry.setComment(dto.getComment());
            logEntryRepository.save(logEntry);
            log.info("Log Entry successfully updated!");
            return ResponseEntity.ok("Successfully evaluated!");
        }
        else{
            log.error("Student with matric number {} not found in assigned students list", matricNo);
            throw new RuntimeException("This student is not assigned to you!");
        }
    }

    @Override
    public ResponseEntity<WeeklySummaryViewDto> viewStudentWeeklySummary(String email, String matricNo, int weekNo) {
        Student student = studentRepository.findByMatricNumber(matricNo);
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (student == null) {
            log.error("Student with matric number {} not found in student Repo", matricNo);
            throw new RuntimeException("Student with MatricNumber " + matricNo + " not found");
        }
        if (!supervisor.getStudents().contains(student)) {
            log.error("Student with matricNumber {} not found in supervisor's list of assigned students", matricNo);
            throw new RuntimeException("This student is not assigned to you!");
        }
        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNo, student);
        if (weeklySummary == null) {
            log.error("Weekly summary not found in repo");
            throw new WeeklySummaryNotFoundException("Weekly summary not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(summaryDisplayMapper.toWeeklySummary(weeklySummary));
    }

    @Override
    public ResponseEntity<List<SupervisorAssignedStudentDto>> viewAssignedStudents(String email) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            log.error("Supervisor with email {} not found in supervisor repository", email);
            throw new SupervisorNotFoundException("Supervisor not found");
        }
        List<Student> students = supervisor.getStudents();
        if (students.isEmpty()) {
            log.error("Student list is empty");
            throw new StudentNotFoundException("No students found");
        }
        List<SupervisorAssignedStudentDto> supervisorAssignedStudentDtoList = new ArrayList<>();
        for (Student student : students) {
            supervisorAssignedStudentDtoList.add(studentToSupervisorViewMapper.toSupervisorAssignedStudentDto(student));
        }
        log.info("Assigned students list retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(supervisorAssignedStudentDtoList);
    }

    @Override
    public ResponseEntity<String> updateProfile(String email, Map<String, Object> updates) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            log.error("Supervisor with email {} not found in supervisor Repository", email);
            throw new SupervisorNotFoundException("Supervisor not found");
        }
        if (updates.containsKey("name")){
            supervisor.setName((String) updates.get("name"));
            log.info("Name updated successfully to {}",  updates.get("name"));
        }
        if (updates.containsKey("email")){
            supervisor.setEmail((String) updates.get("email"));
            log.info("Email updated successfully to {}",  updates.get("email"));
        }
        supervisorRepository.save(supervisor);
        log.info("User profile updated successfully");
        return ResponseEntity.ok("Successfully updated");
    }
}
