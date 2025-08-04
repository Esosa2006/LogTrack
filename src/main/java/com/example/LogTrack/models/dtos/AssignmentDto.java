package com.example.LogTrack.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignmentDto {
    @NotBlank(message = "Student email is required!")
    private String studentEmail;
    @NotBlank(message = "Supervisor email is required!")
    private String supervisorEmail;
}
