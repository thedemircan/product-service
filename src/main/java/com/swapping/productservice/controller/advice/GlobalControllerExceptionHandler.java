package com.swapping.productservice.controller.advice;

import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.exception.SwappingException;
import com.swapping.productservice.model.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(SwappingException.class)
    public ResponseEntity<ErrorResponse> handleSwappingException(SwappingException exception) {
        log.error("A SwappingException occured " + exception.getKey(), exception);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception(exception.getClass().getName())
                .error(messageSource.getMessage(exception.getKey(), exception.getArgs(), new Locale("tr")))
                .build();
        return ResponseEntity.status(exception.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException occurred", exception);
        final List<String> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(this::getMessage)
                .collect(Collectors.toList());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception(exception.getClass().getName())
                .errors(errorMessages)
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SwappingDomainNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSwappingDomainNotFoundException(SwappingDomainNotFoundException exception) {
        log.error("SwappingDomainNotFoundException occurred", exception);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception(exception.getClass().getName())
                .error(messageSource.getMessage(exception.getKey(), exception.getArgs(), new Locale("tr")))
                .build();
        return ResponseEntity.status(exception.getHttpStatus()).body(errorResponse);
    }

    private String getMessage(FieldError error) {
        final String messageKey = error.getDefaultMessage();
        return messageSource.getMessage(messageKey, error.getArguments(), messageKey, new Locale("tr"));
    }
}
