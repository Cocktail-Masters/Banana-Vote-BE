package com.cocktailmasters.backend.util.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cocktailmasters.backend.util.exception.AuthException;
import com.cocktailmasters.backend.util.exception.NotAdminException;
import com.fasterxml.jackson.core.JsonParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExcpetion(Exception ex) {
        // Exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Server error!!!");
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> handleJsonParseException(JsonParseException ex) {
        return ResponseEntity.badRequest().body("request format is invalid");
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException ex) {
        // need to login
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("not authorized");
    }

    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<String> handleNotAdminException(NotAdminException ex) {
        // need to login
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("not admin");
    }
}
