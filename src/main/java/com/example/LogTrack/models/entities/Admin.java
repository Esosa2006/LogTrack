package com.example.LogTrack.models.entities;

import com.example.LogTrack.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_no")
    private String phone_no;
    @Column(name = "role")
    private Role role;
}
