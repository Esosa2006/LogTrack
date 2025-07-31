package com.example.LogTrack.models.dtos.LogEntries;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogEntryCreationDto {
    @NotBlank(message = "This field cannot be blank!")
    private String activityDescription;
    @NotBlank(message = "Week number cannot be blank!")
    private int weekNumber;
}
