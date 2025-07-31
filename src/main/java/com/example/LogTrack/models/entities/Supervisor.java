package com.example.LogTrack.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

@Entity
public class Supervisor extends AppUser{
    @Getter
    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL)
    private List<Student> students;
}
