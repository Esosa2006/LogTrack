package com.example.LogTrack.exceptions.exceptions;

public class AlreadyExistingEntryFoundException extends RuntimeException {
    public AlreadyExistingEntryFoundException(String message) {
        super(message);
    }
}
