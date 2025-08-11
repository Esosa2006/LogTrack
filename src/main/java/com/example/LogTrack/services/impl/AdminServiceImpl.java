package com.example.LogTrack.services.impl;

import com.example.LogTrack.exceptions.exceptions.*;
import com.example.LogTrack.mapper.ToStudentViewDtoMapper;
import com.example.LogTrack.mapper.ToSupervisorDtoMapper;
import com.example.LogTrack.models.dtos.AssignmentDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.SupervisorRepository;
import com.example.LogTrack.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final StudentRepository studentRepository;
    private final SupervisorRepository supervisorRepository;
    private final ToSupervisorDtoMapper toSupervisorDtoMapper;
    private final ToStudentViewDtoMapper toStudentViewDtoMapper;

    @Autowired
    public AdminServiceImpl(StudentRepository studentRepository, SupervisorRepository supervisorRepository, ToSupervisorDtoMapper toSupervisorDtoMapper, ToStudentViewDtoMapper toStudentViewDtoMapper) {
        this.studentRepository = studentRepository;
        this.supervisorRepository = supervisorRepository;
        this.toSupervisorDtoMapper = toSupervisorDtoMapper;
        this.toStudentViewDtoMapper = toStudentViewDtoMapper;
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


//    @Override
//    public ResponseEntity<ApiInfoDto> viewSystemInfo() {
//        ApiInfoDto apiInfoDto = new ApiInfoDto();
//        int no_of_students = studentRepository.findAll().size();
//        int no_of_supervisors = supervisorRepository.findAll().size();
//        apiInfoDto.setNo_of_active_accounts(no_of_students +  no_of_supervisors);
//
//    }

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
