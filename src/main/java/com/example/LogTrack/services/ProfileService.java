package com.example.LogTrack.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProfileService {
    ResponseEntity<String> updateStudentProfile(String email, Map<String, Object> updates);

    ResponseEntity<String> updateSupervisorProfile(String email, Map<String, Object> updates);
}
