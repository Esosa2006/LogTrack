package com.example.LogTrack.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EntryDisplayDto {
    private LocalDate date;
    private String studentName;
    private String comment;
}
