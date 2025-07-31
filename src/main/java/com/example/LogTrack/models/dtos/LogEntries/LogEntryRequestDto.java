package com.example.LogTrack.models.dtos.LogEntries;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogEntryRequestDto {
    @NotBlank(message = "Week number cannot be blank!")
    private int weekNo;
    @NotBlank(message = "Day number cannot be blank!")
    private int dayNo;
}
