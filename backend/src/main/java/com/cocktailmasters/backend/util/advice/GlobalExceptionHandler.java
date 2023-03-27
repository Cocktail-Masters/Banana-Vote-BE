package com.cocktailmasters.backend.util.advice;

import com.cocktailmasters.backend.util.exception.AuthException;
import com.cocktailmasters.backend.util.exception.NotAdminException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExcpetion(Exception ex) {
        // Exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Server error!!!");
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