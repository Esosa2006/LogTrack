package com.example.LogTrack.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface StudentService {
    ResponseEntity<String> updateProfile(String email, Map<String, Object> updates);
}
