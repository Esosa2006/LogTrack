package com.example.LogTrack.services;

import com.example.LogTrack.mapper.SummaryDisplayMapper;
import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.Student;
import com.example.LogTrack.models.entities.WeeklySummary;
import com.example.LogTrack.repositories.LogEntryRepository;
import com.example.LogTrack.repositories.StudentRepository;
import com.example.LogTrack.repositories.WeeklySummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WeeklySummaryService {
    private final WeeklySummaryRepository weeklySummaryRepository;
    private final StudentRepository studentRepository;
    private final SummaryDisplayMapper summaryDisplayMapper;

    @Autowired
    public WeeklySummaryService(WeeklySummaryRepository weeklySummaryRepository, StudentRepository studentRepository, SummaryDisplayMapper summaryDisplayMapper) {
        this.weeklySummaryRepository = weeklySummaryRepository;
        this.studentRepository = studentRepository;
        this.summaryDisplayMapper = summaryDisplayMapper;
    }

    public Student addEntryToWeeklySummary(LogEntry logEntry, int weekNumber, Student student) {
        List<WeeklySummary> summaries = student.getWeeklySummaries();

        if (!hasIncompleteWeeklySummary(summaries)) {
            WeeklySummary newSummary = new WeeklySummary();
            newSummary.setStudent(student);
            newSummary.setWeekNumber(weekNumber);
            newSummary.getEntries().add(logEntry);
            logEntry.setWeeklySummary(newSummary);
            summaries.add(newSummary);
        }

        else{
            WeeklySummary targetSummary = getWeeklySummaryNotUpToFive(summaries);
            if (targetSummary != null) {
                targetSummary.getEntries().add(logEntry);
                logEntry.setWeeklySummary(targetSummary);
            } else {
                WeeklySummary newSummary = new WeeklySummary();
                newSummary.setStudent(student);
                newSummary.setWeekNumber(summaries.size() + 1);
                newSummary.getEntries().add(logEntry);
                logEntry.setWeeklySummary(newSummary);
                summaries.add(newSummary);
            }
        }
        return studentRepository.save(student);
    }


    private WeeklySummary getWeeklySummaryNotUpToFive(List<WeeklySummary> weeklySummaries){
        for (WeeklySummary summary : weeklySummaries){
            if (summary.getEntries().size() < 7){
                return summary;
            }
        }
        return null;
    }

    private boolean hasIncompleteWeeklySummary(List<WeeklySummary> weeklySummaries){
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
        StringBuilder finalSummary = new StringBuilder();
        for (LogEntry entry : weeklySummary.getEntries()){
            String eachSummary = String.format("""
                %s,
                %s,
                %s,
                ------------
                """, entry.getDate(), weeklySummary.getStudent().getName(), entry.getActivityDescription());
            finalSummary.append(eachSummary);
        }
        weeklySummary.setSummaryText(finalSummary.toString());
        return weeklySummary;
    }
}
