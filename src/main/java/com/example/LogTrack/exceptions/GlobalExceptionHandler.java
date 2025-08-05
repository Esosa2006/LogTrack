package com.example.LogTrack.exceptions;

import com.example.LogTrack.exceptions.exceptions.*;
import com.example.LogTrack.models.entities.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AuthenticationFailedException.class)
    public ResponseEntity<Object> handleAuthenticationFailedException(AuthenticationFailedException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = OnlyOneFieldAllowedException.class)
    public ResponseEntity<Object> handleOnlyOneFieldAllowedException(OnlyOneFieldAllowedException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = FieldNotFoundException.class)
    public ResponseEntity<Object> handleFieldNotFoundException(FieldNotFoundException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = EmptyRepoException.class)
    public ResponseEntity<Object> handleEmptyRepoException(EmptyRepoException e){
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = EmailAlreadyInUseException.class)
    public ResponseEntity<Object> handleEmailAlreadyInUseException(EmailAlreadyInUseException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = EntryAlreadyApprovedException.class)
    public ResponseEntity<Object> handleEntryAlreadyApprovedException(EntryAlreadyApprovedException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = FieldRestrictionException.class)
    public ResponseEntity<Object> handleFieldRestrictionException(FieldRestrictionException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = InvalidDayNumberException.class)
    public ResponseEntity<Object> handleInvalidDayNumberException(InvalidDayNumberException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = MatricNumberNotFoundException.class)
    public ResponseEntity<Object> handleMatricNumberNotFoundException(MatricNumberNotFoundException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = NoLogEntryFoundException.class)
    public ResponseEntity<Object> handleNoLogEntryException(NoLogEntryFoundException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = PasswordMissmatchException.class)
    public ResponseEntity<Object> handlePasswordMissmatchException(PasswordMissmatchException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = StudentNotFoundException.class)
    public ResponseEntity<Object> handleStudentNotFoundException(StudentNotFoundException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = SupervisorNotFoundException.class)
    public ResponseEntity<Object> handleSupervisorNotFoundException(SupervisorNotFoundException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = StudentNotUnderSupervisorException.class)
    public ResponseEntity<Object> handleStudentNotUnderSupervisorException(StudentNotUnderSupervisorException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = WeeklySummaryNotFoundException.class)
    public ResponseEntity<Object> handleWeeklySummaryNotFoundException(WeeklySummaryNotFoundException e) {
        Exception z = new Exception(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(z, HttpStatus.BAD_REQUEST);
    }
}
