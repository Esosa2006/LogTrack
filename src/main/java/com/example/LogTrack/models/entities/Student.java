package com.example.LogTrack.models.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Entity
public class Student extends AppUser {
    @Getter
    @Column(name = "matricNumber")
    private String matricNumber;

    @Getter
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<LogEntry> logEntries;

    @Getter
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<WeeklySummary> weeklySummaries;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
}
