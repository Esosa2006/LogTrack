package com.example.LogTrack.models.entities;

import com.example.LogTrack.enums.TokenType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table
@Entity
public class Token {
    public Token() {
        this.createdAt = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long tokenId;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "tokenType", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false)
    private AppUser user;
    @Column(name = "used", nullable = false)
    private boolean used;
    @Column(name = "expiry")
    private LocalDateTime expiry;
    @Column(name = "revoked")
    private boolean revoked;
}
