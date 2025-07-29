package com.example.LogTrack.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyEntrySummary {
    private Long id;
    private LocalDate date;
    private String studentName;
    private String matricNumber;
    private String comment;
}
