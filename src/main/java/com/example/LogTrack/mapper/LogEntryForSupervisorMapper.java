package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.logEntries.SupervisorLogEntryView;
import com.example.LogTrack.models.entities.LogEntry;
import org.springframework.stereotype.Component;

@Component
public class LogEntryForSupervisorMapper {
    public SupervisorLogEntryView toLogEntryView(LogEntry logEntry) {
        SupervisorLogEntryView logEntryView = new SupervisorLogEntryView();
        logEntryView.setEntryStatus(logEntry.getStatus());
        logEntryView.setActivityDescription(logEntry.getActivityDescription());
        logEntryView.setComment(logEntry.getComment());
        logEntryView.setStudentName(logEntry.getStudent().getName());
        logEntryView.setDate(logEntry.getDate());
        return logEntryView;
    }
}
