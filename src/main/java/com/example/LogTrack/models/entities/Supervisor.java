package com.example.LogTrack.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Supervisor extends AppUser{
    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL)
    private List<Student> students;
}
