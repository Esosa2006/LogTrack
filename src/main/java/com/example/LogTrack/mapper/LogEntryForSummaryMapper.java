package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.EntryDisplayDto;
import com.example.LogTrack.models.entities.LogEntry;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LogEntryForSummaryMapper {
    public EntryDisplayDto toEntryDisplayDto(LogEntry logEntry) {
        EntryDisplayDto entryDisplayDto = new EntryDisplayDto();
        entryDisplayDto.setComment(logEntry.getComment());
        entryDisplayDto.setStudentName(logEntry.getStudent().getName());
        entryDisplayDto.setDate(logEntry.getDate());
        return  entryDisplayDto;
    }
}
