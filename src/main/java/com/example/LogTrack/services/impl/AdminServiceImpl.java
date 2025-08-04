package com.example.LogTrack.services.impl;

import com.example.LogTrack.exceptions.exceptions.EmptyRepoException;
import com.example.LogTrack.exceptions.exceptions.StudentNotFoundException;
import com.example.LogTrack.exceptions.exceptions.SupervisorNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
