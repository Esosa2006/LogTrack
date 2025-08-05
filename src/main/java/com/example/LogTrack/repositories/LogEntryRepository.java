package com.example.LogTrack.repositories;

import com.example.LogTrack.enums.EntryStatus;
import com.example.LogTrack.models.entities.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    @Query("SELECT le FROM LogEntry le " +
            "WHERE le.status = :status " +
            "AND le.weeklySummary.student.id = :studentId")
    List<LogEntry> findByStatusAndStudentId(@Param("status") EntryStatus status,
                                            @Param("studentId") Long studentId);
}
