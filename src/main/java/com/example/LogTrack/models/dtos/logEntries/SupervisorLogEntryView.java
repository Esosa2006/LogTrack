package com.example.LogTrack.models.dtos.logEntries;

import com.example.LogTrack.enums.EntryStatus;

import java.time.LocalDate;

public record SupervisorLogEntryView(LocalDate date, String studentName, EntryStatus entryStatus, String activityDescription, String comment) {
}
