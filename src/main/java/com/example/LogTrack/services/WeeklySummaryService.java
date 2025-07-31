package com.example.LogTrack.services;

import com.example.LogTrack.mapper.LogEntryForSummaryMapper;
import com.example.LogTrack.mapper.SummaryDisplayMapper;
import com.example.LogTrack.models.dtos.logEntries.EntryDisplayDto;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.WeeklySummary;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.WeeklySummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeeklySummaryService {
    private final WeeklySummaryRepository weeklySummaryRepository;
    private final StudentRepository studentRepository;
    private final LogEntryForSummaryMapper logEntryForSummaryMapper;
    private final SummaryDisplayMapper summaryDisplayMapper;

    @Autowired
    public WeeklySummaryService(WeeklySummaryRepository weeklySummaryRepository, StudentRepository studentRepository, LogEntryForSummaryMapper logEntryForSummaryMapper, SummaryDisplayMapper summaryDisplayMapper) {
        this.weeklySummaryRepository = weeklySummaryRepository;
        this.studentRepository = studentRepository;
        this.logEntryForSummaryMapper = logEntryForSummaryMapper;
        this.summaryDisplayMapper = summaryDisplayMapper;
    }

    public Student addEntryToWeeklySummary(LogEntry logEntry, int weekNumber, Student student) {
        List<WeeklySummary> summaries = student.getWeeklySummaries();

        if (summaries.isEmpty()) {
            WeeklySummary newSummary = new WeeklySummary();
            newSummary.setStudent(student);
            newSummary.setWeekNumber(weekNumber);
            newSummary.getEntries().add(logEntry);
            summaries.add(newSummary);
            weeklySummaryRepository.save(newSummary);
        }

        else if (isNotEmpty(summaries)) {
            WeeklySummary targetSummary = getWeeklySummaryNotUpToFive(summaries);
            if (targetSummary != null) {
                targetSummary.getEntries().add(logEntry);
                weeklySummaryRepository.save(targetSummary);
            } else {
                WeeklySummary newSummary = new WeeklySummary();
                newSummary.setStudent(student);
                newSummary.setWeekNumber(summaries.size() + 1);
                newSummary.getEntries().add(logEntry);
                summaries.add(newSummary);
                weeklySummaryRepository.save(newSummary);
            }
        }
        return student;
    }

    private WeeklySummary getWeeklySummaryNotUpToFive(List<WeeklySummary> weeklySummaries){
        for (WeeklySummary summary : weeklySummaries){
            if (summary.getEntries().size() < 6){
                return summary;
            }
        }
        return null;
    }

    private boolean isNotEmpty(List<WeeklySummary> weeklySummaries){
        for (WeeklySummary summary : weeklySummaries){
            if (summary.getEntries().size() < 6){
                return true;
            }
        }
        return false;
    }

    public ResponseEntity<WeeklySummaryViewDto> getWeeklySummary(String email, int weekNumber) {
        Student student = studentRepository.findByEmail(email);
        WeeklySummary weeklySummary = weeklySummaryRepository.findByWeekNumberAndStudent(weekNumber, student);
        WeeklySummaryViewDto weeklySummaryViewDto = summaryDisplayMapper.toWeeklySummary(generateWeeklySummaryText(weeklySummary));
        return ResponseEntity.status(HttpStatus.OK).body(weeklySummaryViewDto);
    }

    private WeeklySummary generateWeeklySummaryText(WeeklySummary weeklySummary) {
        List<EntryDisplayDto> entries = new ArrayList<>();
        for (LogEntry logEntry : weeklySummary.getEntries()) {
            EntryDisplayDto entryDisplayDto = logEntryForSummaryMapper.toEntryDisplayDto(logEntry, weeklySummary);
            entries.add(entryDisplayDto);
        }
        String summary = entries.toString();
        weeklySummary.setSummaryText(summary);
        return weeklySummary;
    }
}
