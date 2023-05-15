package com.cocktailmasters.backend.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundUserException extends CustomException {
    
    public NotFoundUserException() {
        super(HttpStatus.NOT_FOUND, "not found user");
    }
}
