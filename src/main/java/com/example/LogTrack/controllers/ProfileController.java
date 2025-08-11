package com.example.LogTrack.controllers;

import com.example.LogTrack.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PatchMapping("/student/update")
    public ResponseEntity<String> updateStudentProfile(Authentication authentication,
                                                       @RequestBody Map<String,Object> updates){
        String email = authentication.getName();
        return profileService.updateStudentProfile(email, updates);
    }

    @PatchMapping("/supervisor/update")
    public ResponseEntity<String> updateProfile(Authentication authentication,
                                                @RequestBody Map<String,Object> updates) {
        String email = authentication.getName();
        return profileService.updateSupervisorProfile(email, updates);
    }
}
