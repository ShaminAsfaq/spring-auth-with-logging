package com.example.auth.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Data conflict", "message", ex.getMostSpecificCause().getMessage()));
    }

    // Optional: Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error", "message", ex.getMessage()));
    }
}
