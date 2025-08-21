package com.example.LogTrack.models.entities;

import com.example.LogTrack.enums.DayOfTheWeek;
import com.example.LogTrack.enums.EntryStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Table
@Entity
@Data
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "day")
    private Integer dayNo;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EntryStatus status;
    @Column(name = "activityDescrption")
    private String activityDescription;
    @Column(name = "dayOfTheWeek")
    @Enumerated(EnumType.STRING)
    private DayOfTheWeek day;
    @Column(name = "comment")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "weekly_summary_id")
    private WeeklySummary weeklySummary;
}
