package com.bayu.regulatory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParseValueException extends RuntimeException {

    public ParseValueException() {
        super();
    }

    public ParseValueException(String message) {
        super(message);
    }

    public ParseValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
