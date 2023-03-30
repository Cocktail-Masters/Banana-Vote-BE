package com.cocktailmasters.backend.util.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends CustomException {

    public AuthException() {
        super(HttpStatus.UNAUTHORIZED, "not authorized");
    }
}
