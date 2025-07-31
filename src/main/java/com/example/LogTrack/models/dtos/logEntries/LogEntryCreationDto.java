package com.example.LogTrack.models.dtos.logEntries;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogEntryCreationDto {
    @NotBlank(message = "This field cannot be blank!")
    private String activityDescription;
    private int weekNumber;
}
