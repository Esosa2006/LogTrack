package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.SupervisorAssignedStudentDto;
import com.example.LogTrack.models.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentToSupervisorViewMapper {
    public SupervisorAssignedStudentDto toSupervisorAssignedStudentDto(Student student) {
        SupervisorAssignedStudentDto studentDto = new SupervisorAssignedStudentDto();
        studentDto.setEmail(student.getEmail());
        studentDto.setName(student.getName());
        studentDto.setMatricNo(student.getMatricNumber());
        return studentDto;
    }
}
