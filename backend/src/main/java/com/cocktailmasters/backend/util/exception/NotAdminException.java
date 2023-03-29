package com.cocktailmasters.backend.util.exception;

import org.springframework.http.HttpStatus;

public class NotAdminException extends CustomException {

    public NotAdminException() {
        super(HttpStatus.FORBIDDEN, "not admin");
    }
}
