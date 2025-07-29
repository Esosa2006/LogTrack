package com.example.LogTrack.mapper;

import com.example.LogTrack.models.dtos.weeklySummaries.WeeklySummaryViewDto;
import com.example.LogTrack.models.entities.WeeklySummary;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SummaryDisplayMapper {
    public WeeklySummaryViewDto toWeeklySummary(WeeklySummary weeklySummary) {
        WeeklySummaryViewDto weeklySummaryViewDto = new WeeklySummaryViewDto();
        weeklySummaryViewDto.setWeekNumber(weeklySummary.getWeekNumber());
        weeklySummaryViewDto.setSummaryText(weeklySummary.getSummaryText());
        return weeklySummaryViewDto;
    }
}
