package com.example.LogTrack.models.dtos.adminViews;

import com.example.LogTrack.enums.Role;

import java.util.List;

public record SupervisorDto(String name, String email , Role role, List<AssignedStudentOverviewDto> students) {
}
