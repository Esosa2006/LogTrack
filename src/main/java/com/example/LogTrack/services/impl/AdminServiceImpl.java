package com.example.LogTrack.services.impl;

import com.example.LogTrack.exceptions.exceptions.*;
import com.example.LogTrack.mapper.ToStudentViewDtoMapper;
import com.example.LogTrack.mapper.ToSupervisorDtoMapper;
import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.models.entities.Token;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.repositories.TokenRepository;
import com.example.LogTrack.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final ToSupervisorDtoMapper toSupervisorDtoMapper;
    private final ToStudentViewDtoMapper toStudentViewDtoMapper;
    private final TokenRepository tokenRepository;

    @Autowired
    public AdminServiceImpl(StudentRepository studentRepository, SupervisorRepository supervisorRepository, ToSupervisorDtoMapper toSupervisorDtoMapper, ToStudentViewDtoMapper toStudentViewDtoMapper, TokenRepository tokenRepository) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.toSupervisorDtoMapper = toSupervisorDtoMapper;
        this.toStudentViewDtoMapper = toStudentViewDtoMapper;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ResponseEntity<String> assignStudentToSupervisor(AssignmentDto assignmentDto) {
        Student student = studentRepository.findByEmail(assignmentDto.getStudentEmail());
        if (student == null) {
            throw new StudentNotFoundException("Student not found!");
        }
        Supervisor supervisor = supervisorRepository.findByEmail(assignmentDto.getSupervisorEmail());
        if (supervisor == null) {
            throw new SupervisorNotFoundException("Supervisor not found!");
        }
        supervisor.getStudents().add(student);
        student.setSupervisor(supervisor);
        supervisorRepository.save(supervisor);
        return ResponseEntity.status(HttpStatus.OK).body("Mr / Mrs " + supervisor.getName() + " has successfully been assigned to " + student.getName());
    }

    @Override
    public List<SupervisorDto> viewAllSupervisors() {
        if (supervisorRepository.findAll().isEmpty()) {
            throw new EmptyRepoException("No supervisor found!");
        }
        return supervisorRepository.findAll().stream().map(toSupervisorDtoMapper::toDto).toList();
    }

    @Override
    public List<StudentViewDto> viewAllStudents() {
        if (studentRepository.findAll().isEmpty()) {
            throw new EmptyRepoException("No students found!");
        }
        return studentRepository.findAll().stream().map(toStudentViewDtoMapper::toDto).toList();
    }

    @Override
    public List<StudentViewDto> searchForStudentsByField(Map<String, Object> search){
        if (search.size() > 1){
            throw new OnlyOneFieldAllowedException("Only one field can be passed");
        }
        if (search.containsKey("name")) {
            List<StudentViewDto> list = studentRepository.findAllByName(search.get("name").toString()).stream().map(toStudentViewDtoMapper::toDto).toList();
            emptyListCheck(list);
            return list;
        }
        else if (search.containsKey("email")){
            List<StudentViewDto> list = studentRepository.findAllByEmail(search.get("email").toString()).stream().map(toStudentViewDtoMapper::toDto).toList();
            emptyListCheck(list);
            return list;
        }
        else if (search.containsKey("matricNumber")){
            List<StudentViewDto> list =  studentRepository.findAllByMatricNumber(search.get("matricNumber").toString()).stream().map(toStudentViewDtoMapper::toDto).toList();
            emptyListCheck(list);
            return list;
        }
        else if (search.containsKey("supervisorName")){
            List<StudentViewDto> list =  studentRepository.findAllBySupervisorName(search.get("supervisorName").toString()).stream().map(toStudentViewDtoMapper::toDto).toList();
            emptyListCheck(list);
            return list;
        }
        else if (search.containsKey("supervisorEmail")){
            List<StudentViewDto> list =  studentRepository.findAllBySupervisorEmail(search.get("supervisorEmail").toString()).stream().map(toStudentViewDtoMapper::toDto).toList();
            emptyListCheck(list);
            return list;
        }
        else{
            throw new FieldNotFoundException("No such field found!");
        }
    }

    @Override
    public List<SupervisorDto> searchForSupervisorByField(Map<String, Object> search) {
        if (search.size() > 1){
            throw new OnlyOneFieldAllowedException("Only one field can be passed");
        }

        if (search.containsKey("name")) {
            List<SupervisorDto> supervisorList = supervisorRepository.findAllByName(search.get("name").toString()).stream().map(toSupervisorDtoMapper::toDto).toList();
            emptySupervisorListCheck(supervisorList);
            return supervisorList;
        }
        else if (search.containsKey("email")){
            List<SupervisorDto> supervisorList = supervisorRepository.findAllByEmail(search.get("email").toString()).stream().map(toSupervisorDtoMapper::toDto).toList();
            emptySupervisorListCheck(supervisorList);
            return supervisorList;
        }
        else{
            throw new FieldNotFoundException("No such field found!");
        }
    }

    @Override
    public ResponseEntity<String> getActiveUserCount() {
        int studentCount = studentRepository.findAll().size();
        log.info("Number of student accounts: {}",  studentCount);
        int supervisorCount = supervisorRepository.findAll().size();
        log.info("Number of supervisor accounts: {}",  supervisorCount);
        String sentence = "Total number of accounts: " + studentCount + supervisorCount;
        return ResponseEntity.status(HttpStatus.OK).body(sentence);
    }

    @Override
    public ResponseEntity<String> revokeToken(Long id) {
        Token token = tokenRepository.findById(id).orElseThrow(() -> new GlobalException("Token not found!"));
        token.setUsed(true);
        token.setRevoked(true);
        tokenRepository.save(token);
        return ResponseEntity.status(HttpStatus.OK).body("Token revoked!");
    }

    @Override
    public ResponseEntity<List<Token>> getTokens(String state) {
        List<Token> expiredTokens = tokenRepository.findAllByExpiryAfter(LocalDateTime.now());
        List<Token> activeTokens = tokenRepository.findAllByUsed(false);
        List<Token> usedTokens = tokenRepository.findAllByUsed(true);
        List<Token> revokedTokens = tokenRepository.findByRevoked(true);
        if (state.equalsIgnoreCase("expired")){
            return ResponseEntity.status(HttpStatus.OK).body(expiredTokens);
        }
        if (state.equalsIgnoreCase("active")){
            return ResponseEntity.status(HttpStatus.OK).body(activeTokens);
        }
        if (state.equalsIgnoreCase("used")){
            return ResponseEntity.status(HttpStatus.OK).body(usedTokens);
        }
        if(state.equalsIgnoreCase("revoked")){
            return ResponseEntity.status(HttpStatus.OK).body(revokedTokens);
        }
        throw new GlobalException("Invalid state!");

    }

    private static void emptyListCheck(List<StudentViewDto> list) {
        if (list.isEmpty()){
            throw new EmptyRepoException("No students found!");
        }
    }

    private static void emptySupervisorListCheck(List<SupervisorDto> list) {
        if (list.isEmpty()){
            throw new EmptyRepoException("No students found!");
        }
    }
}
