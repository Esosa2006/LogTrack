package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student  findByEmail(String email);
}
