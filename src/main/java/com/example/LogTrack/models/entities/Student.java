package com.example.LogTrack.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student extends AppUser {
    @Setter
    @Getter
    @Column(name = "matricNumber")
    private String matricNumber;

    @Setter
    @Getter
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklySummary> weeklySummaries = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;
}
