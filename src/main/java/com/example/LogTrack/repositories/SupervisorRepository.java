package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor,Long> {
    Supervisor findByEmail(String email);
    List<Supervisor> findAllByName(String name);
    List<Supervisor> findAllByEmail(String email);
}
