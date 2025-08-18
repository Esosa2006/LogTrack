package com.example.LogTrack.models.dtos.logEntries;

import com.example.LogTrack.enums.EntryStatus;

import java.time.LocalDate;

public record DailyLogEntryDto(Long id, LocalDate date, String comment, EntryStatus entryStatus, String activityDescription) {
}
