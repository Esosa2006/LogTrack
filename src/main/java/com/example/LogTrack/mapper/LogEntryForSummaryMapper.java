package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.logEntries.EntryDisplayDto;
import com.example.LogTrack.models.entities.LogEntry;
import com.example.LogTrack.models.entities.WeeklySummary;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LogEntryForSummaryMapper {
    public EntryDisplayDto toEntryDisplayDto(LogEntry logEntry, WeeklySummary weeklySummary) {
        EntryDisplayDto entryDisplayDto = new EntryDisplayDto();
        entryDisplayDto.setActivityDescription(logEntry.getActivityDescription());
        entryDisplayDto.setStudentName(weeklySummary.getStudent().getName());
        entryDisplayDto.setDate(logEntry.getDate());
        return  entryDisplayDto;
    }
}
