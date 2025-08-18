package com.example.LogTrack.models.dtos.adminViews;

import com.example.LogTrack.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class SupervisorDto {
    private String name;
    private String email;
    private Role role;
    private List<AssignedStudentOverviewDto> students;
}
