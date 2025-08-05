package com.example.LogTrack.repositories;

import com.example.LogTrack.enums.EntryStatus;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.WeeklySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklySummaryRepository extends JpaRepository<WeeklySummary,Long> {
    WeeklySummary findByWeekNumberAndStudent(Integer weekNo, Student student);
}
