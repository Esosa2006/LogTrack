package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student  findByEmail(String email);
    Student findByMatricNumber(String matricNumber);
    List<Student> findAllByName(String name);
    List<Student> findAllByEmail(String email);
    List<Student> findAllByMatricNumber(String matricNumber);
    List<Student> findAllBySupervisorName(String supervisorName);
    List<Student> findAllBySupervisorEmail(String supervisorEmail);

    Student findByVerificationToken(String token);
}
