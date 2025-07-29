package com.example.LogTrack.models.dtos.LogEntries;

import lombok.Data;

@Data
public class LogEntryCreationDto {
    private String activityDescription;
    private int weekNumber;
}
