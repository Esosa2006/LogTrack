package com.example.LogTrack.services.impl;

import com.example.LogTrack.enums.EntryStatus;
import com.example.LogTrack.exceptions.exceptions.StudentNotFoundException;
import com.example.LogTrack.exceptions.exceptions.SupervisorNotFoundException;
import com.example.LogTrack.mapper.LogEntryForSupervisorMapper;
import com.example.LogTrack.mapper.StudentToSupervisorViewMapper;
import com.example.LogTrack.mapper.SummaryDisplayMapper;
import com.example.LogTrack.models.dtos.EvaluationDto;
import com.example.LogTrack.models.dtos.SupervisorAssignedStudentDto;
import com.example.LogTrack.models.dtos.authDtos.StudentSignUpRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            throw new RuntimeException("Supervisor not found");
        }
        Student student = studentRepository.findByMatricNumber(matricNo);
        if (student == null) {
            throw new RuntimeException("Student with MatricNumber " + matricNo + " not found");
        }

        if (supervisor.getStudents().contains(student)) {
            List<SupervisorLogEntryView> logEntryViewList = new ArrayList<>();
            for (WeeklySummary weeklySummary : student.getWeeklySummaries()){
                for (LogEntry entry : weeklySummary.getEntries()){
                    logEntryViewList.add(logEntryForSupervisorMapper.toLogEntryView(entry, weeklySummary));
                }
            }
            return ResponseEntity.ok(logEntryViewList);
        }
        throw new RuntimeException("This student is not assigned to you!");
    }

    @Override
    public ResponseEntity<String> evaluateEntry(int weekNo, int dayNo, String matricNo, String email, EvaluationDto dto) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            throw new RuntimeException("Supervisor not found");
        }
        Student student = studentRepository.findByMatricNumber(matricNo);
        if (student == null) {
            throw new RuntimeException("Student with MatricNumber " + matricNo + " not found");
        }
        if (supervisor.getStudents().contains(student)) {
            WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNo, student);
            if (weeklySummary == null) {
                throw new RuntimeException("Weekly summary not found");
            }
            List<LogEntry> entries = weeklySummary.getEntries();
            if (entries == null || dayNo < 1 || dayNo > entries.size()) {
                throw new RuntimeException("Invalid day number: " + dayNo);
            }
            LogEntry logEntry = entries.get(dayNo - 1);

            if (logEntry == null) {
                throw new RuntimeException("Log entry for day " + dayNo + " not found");
            }
            if (dto.getStatus().equalsIgnoreCase("Approved")){
                logEntry.setStatus(EntryStatus.APPROVED);
            }
            else if (dto.getStatus().equalsIgnoreCase("Rejected")){
                logEntry.setStatus(EntryStatus.REJECTED);
            }
            else{
                logEntry.setStatus(EntryStatus.PENDING);
            }
            logEntry.setComment(dto.getComment());
            logEntryRepository.save(logEntry);
            return ResponseEntity.ok("Successfully updated");
        }
        else{
            throw new RuntimeException("This student is not assigned to you!");
        }
    }

    @Override
    public ResponseEntity<WeeklySummaryViewDto> viewStudentWeeklySummary(String email, String matricNo, int weekNo) {
        Student student = studentRepository.findByMatricNumber(matricNo);
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (student == null) {
            throw new RuntimeException("Student with MatricNumber " + matricNo + " not found");
        }
        if (!supervisor.getStudents().contains(student)) {
            throw new RuntimeException("This student is not assigned to you!");
        }
        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNo, student);
        return ResponseEntity.status(HttpStatus.OK).body(summaryDisplayMapper.toWeeklySummary(weeklySummary));
    }

    @Override
    public ResponseEntity<List<SupervisorAssignedStudentDto>> viewAssignedStudents(String email) {
        Supervisor supervisor = supervisorRepository.findByEmail(email);
        if (supervisor == null) {
            throw new SupervisorNotFoundException("Supervisor not found");
        }
        List<Student> students = supervisor.getStudents();
        if (students.isEmpty()) {
            throw new StudentNotFoundException("No students found");
        }
        List<SupervisorAssignedStudentDto> supervisorAssignedStudentDtoList = new ArrayList<>();
        for (Student student : students) {
            supervisorAssignedStudentDtoList.add(studentToSupervisorViewMapper.toSupervisorAssignedStudentDto(student));
        }
        return ResponseEntity.status(HttpStatus.OK).body(supervisorAssignedStudentDtoList);
    }
}
