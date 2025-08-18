package com.example.LogTrack.models.dtos.adminViews;

import com.example.LogTrack.enums.Role;
import lombok.Data;

@Data
public class StudentViewDto {
    private String name;
    private String matricNumber;
    private String email;
    private Role role;
    private AssignedSupervisorOverviewDto  supervisor;
}
