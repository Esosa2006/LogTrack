package com.example.LogTrack.security;

import com.example.LogTrack.utils.SecretKeyGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JWTService {
    private final String secretKey = SecretKeyGenerator.generateSecretKey();
    public String generateToken(String email) {
        Map<String,  Object> claims = new HashMap<String, Object>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()* 36000))
                .and()
                .signWith(getKey())
                .compact();
    }
    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims (String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String getUsername(String token) {
        log.info(getClaims(token).getSubject());
        return getClaims(token).getSubject();
    }
    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }
    private boolean isExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String email = getUsername(token);
        return userDetails.getUsername().equals(email) && !isExpired(token);
    }
}
