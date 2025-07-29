package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.LogEntries.EntryDisplayDto;
import com.example.LogTrack.models.entities.LogEntry;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LogEntryForSummaryMapper {
    public EntryDisplayDto toEntryDisplayDto(LogEntry logEntry) {
        EntryDisplayDto entryDisplayDto = new EntryDisplayDto();
        entryDisplayDto.setActivityDescription(logEntry.getActivityDescription());
        entryDisplayDto.setStudentName(logEntry.getStudent().getName());
        entryDisplayDto.setDate(logEntry.getDate());
        return  entryDisplayDto;
    }
}
