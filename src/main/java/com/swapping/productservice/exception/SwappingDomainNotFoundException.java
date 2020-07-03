package com.swapping.productservice.exception;

import org.springframework.http.HttpStatus;

public class SwappingDomainNotFoundException extends SwappingException {

    public SwappingDomainNotFoundException(String key) {
        super(key, HttpStatus.NOT_FOUND);
    }
}