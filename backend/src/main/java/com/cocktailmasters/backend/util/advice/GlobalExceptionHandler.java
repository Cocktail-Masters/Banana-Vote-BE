package com.cocktailmasters.backend.util.advice;

import com.cocktailmasters.backend.util.exception.AuthException;
import com.cocktailmasters.backend.util.exception.NotAdminException;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cocktailmasters.backend.util.exception.CustomException;
import com.fasterxml.jackson.core.JsonParseException;

@RestControllerAdvice
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleJsonParseException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleInvalidParameterException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode()).body(ex.getMessage());
    }
}
