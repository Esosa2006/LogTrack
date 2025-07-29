package com.example.LogTrack.repositories;

import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    LogEntry findByStudentAndId(Student student, Long id);
}
