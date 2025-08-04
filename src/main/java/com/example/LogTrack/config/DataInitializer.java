package com.example.LogTrack.config;

import com.example.LogTrack.enums.Role;
import com.example.LogTrack.models.entities.Admin;
import com.example.LogTrack.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {
    private final AdminRepository adminRepository;

    @Autowired
    public DataInitializer(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {
            String adminEmail = "admin@logtrack.com";
            if (!adminRepository.existsByEmail(adminEmail)) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                Admin admin = new Admin();
                admin.setEmail(adminEmail);
                admin.setName("Admin");
                admin.setPhone_no("09013050110");
                admin.setPassword(encoder.encode("sybaulilnigga"));
                admin.setRole(Role.ADMIN);
                adminRepository.save(admin);
                System.out.println("Admin user created!");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}
