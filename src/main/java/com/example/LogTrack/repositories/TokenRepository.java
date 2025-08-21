package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findTokenByToken(String token);
}
