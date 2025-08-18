package com.example.LogTrack.models.dtos.adminViews;

import com.example.LogTrack.enums.Role;

public record StudentViewDto(String name, String matricNumber, String email, Role role, AssignedStudentOverviewDto assignedStudentOverviewDto) {
}
