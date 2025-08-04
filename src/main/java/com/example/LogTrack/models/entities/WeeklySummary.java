package com.example.LogTrack.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "weekNumber")
    private int weekNumber;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToMany(mappedBy = "weeklySummary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LogEntry> entries = new ArrayList<>();
    @Column(name = "summaryText")
    private String summaryText;
}
