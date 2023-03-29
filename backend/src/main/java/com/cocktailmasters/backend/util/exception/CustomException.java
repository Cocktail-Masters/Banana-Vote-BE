package com.cocktailmasters.backend.util.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    private HttpStatus errorCode;

    public CustomException(HttpStatus errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
