package com.kwetter.userservice.exception;

public class InvalidUserReferenceException extends RuntimeException {
    public InvalidUserReferenceException(String errorMessage) {
        super(errorMessage);
    }
}
