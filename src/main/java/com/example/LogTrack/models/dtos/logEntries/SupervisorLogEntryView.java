package com.example.LogTrack.models.dtos.logEntries;

import com.example.LogTrack.enums.EntryStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SupervisorLogEntryView {
    private LocalDate  date;
    private String studentName;
    private EntryStatus entryStatus;
    private String activityDescription;
    private String comment;
}
