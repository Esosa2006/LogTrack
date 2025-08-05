package com.example.LogTrack.exceptions.exceptions;

public class OnlyOneFieldAllowedException extends RuntimeException {
    public OnlyOneFieldAllowedException(String message) {
        super(message);
    }
}
