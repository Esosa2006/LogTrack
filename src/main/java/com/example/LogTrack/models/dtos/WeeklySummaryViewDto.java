package com.example.LogTrack.models.dtos;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class WeeklySummaryViewDto {
    private int weekNumber;
    private String summaryText;
}
