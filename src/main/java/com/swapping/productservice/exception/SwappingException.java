package com.swapping.productservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SwappingException extends RuntimeException {

    private final String key;
    private final String[] args;
    private final HttpStatus httpStatus;

    public SwappingException(String key, HttpStatus httpStatus, String... args) {
        this.key = key;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public SwappingException(String key, HttpStatus httpStatus) {
        this.key = key;
        this.httpStatus = httpStatus;
        this.args = new String[0];
    }
}