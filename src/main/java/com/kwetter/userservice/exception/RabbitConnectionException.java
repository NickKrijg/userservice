package com.kwetter.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Could not connect with RabbitMQ")
public class RabbitConnectionException extends RuntimeException {
    public RabbitConnectionException(String message) {
        super(message);
    }
}
