package com.example.LogTrack.exceptions.exceptions;

public class NoLogEntryFoundException extends RuntimeException {
    public NoLogEntryFoundException(String message) {
        super(message);
    }
}
