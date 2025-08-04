package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.adminViews.AssignedStudentOverviewDto;
import com.example.LogTrack.models.dtos.adminViews.SupervisorDto;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.Supervisor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ToSupervisorDtoMapper {
    public SupervisorDto toDto(Supervisor supervisor) {
        SupervisorDto dto = new SupervisorDto();
        dto.setEmail(supervisor.getEmail());
        dto.setRole(supervisor.getRole());
        dto.setName(supervisor.getName());
        List<AssignedStudentOverviewDto>  students = new ArrayList<>();
        for (Student student : supervisor.getStudents()) {
            AssignedStudentOverviewDto studentDto = new AssignedStudentOverviewDto();
            studentDto.setName(student.getName());
            studentDto.setMatricNumber(student.getMatricNumber());
            students.add(studentDto);
        }
        dto.setStudents(students);
        return dto;
    }
}
