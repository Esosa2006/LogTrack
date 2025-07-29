package com.example.LogTrack.models.dtos;

import lombok.Data;

@Data
public class LogEntryCreationDto {
    private String comment;
    private int weekNumber;
}
