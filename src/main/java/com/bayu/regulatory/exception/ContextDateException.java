package com.bayu.regulatory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContextDateException extends RuntimeException {

    public ContextDateException() {
        super();
    }

    public ContextDateException(String message) {
        super(message);
    }

    public ContextDateException(String message, Throwable cause) {
        super(message, cause);
    }

}
