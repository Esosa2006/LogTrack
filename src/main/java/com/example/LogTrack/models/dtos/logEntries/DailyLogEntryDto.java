package com.example.LogTrack.models.dtos.logEntries;

import com.example.LogTrack.enums.EntryStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyLogEntryDto {
    private Long id;
    private LocalDate date;
    private String comment;
    private EntryStatus status;
    private String activityDescription;
}
