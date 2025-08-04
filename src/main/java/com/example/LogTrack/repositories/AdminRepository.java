package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    boolean existsByEmail(String adminEmail);
    Admin findByEmail(String adminEmail);
}
