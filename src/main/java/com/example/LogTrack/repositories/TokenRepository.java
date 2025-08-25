package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findTokenByToken(String token);

    List<Token> findAllByExpiryAfter(LocalDateTime dateTime);

    List<Token> findAllByUsed(boolean used);

    List<Token> findByRevoked(boolean revoked);
}

