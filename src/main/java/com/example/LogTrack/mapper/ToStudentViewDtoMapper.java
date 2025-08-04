package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.adminViews.AssignedSupervisorOverviewDto;
import com.example.LogTrack.models.dtos.adminViews.StudentViewDto;
import com.example.LogTrack.models.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class ToStudentViewDtoMapper {
    public StudentViewDto toDto(Student student) {
        StudentViewDto dto = new StudentViewDto();
        dto.setEmail(student.getEmail());
        dto.setRole(student.getRole());
        dto.setName(student.getName());
        dto.setMatricNumber(student.getMatricNumber());
        AssignedSupervisorOverviewDto supervisor = new AssignedSupervisorOverviewDto();
        supervisor.setEmail(student.getSupervisor().getEmail());
        supervisor.setName(student.getSupervisor().getName());
        dto.setSupervisor(supervisor);
        return dto;
    }
}
