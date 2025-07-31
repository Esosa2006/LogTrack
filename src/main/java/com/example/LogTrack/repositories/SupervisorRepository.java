package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor,Integer> {
    Supervisor findByEmail(String email);
}
