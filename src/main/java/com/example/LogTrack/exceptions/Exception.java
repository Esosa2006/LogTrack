package com.example.LogTrack.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record Exception(String message, HttpStatus httpStatus, ZonedDateTime zonedDateTime) {

}
