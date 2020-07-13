package com.swapping.productservice.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.exception.SwappingException;
import com.swapping.productservice.model.error.ErrorResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerExceptionHandler {

    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

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

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleException(FeignException exception) {
        log.error("Api client exception occurred. Detail: " + getFeignExceptionContent(exception), exception);
        return new ResponseEntity<>(resolve(exception), HttpStatus.valueOf(exception.status()));
    }

    private String getFeignExceptionContent(FeignException exception) {
        return Objects.nonNull(exception.content()) ? exception.contentUTF8() : exception.toString();
    }

    private ErrorResponse resolve(FeignException exception) {
        try {
            return objectMapper.readValue(getFeignExceptionContent(exception), ErrorResponse.class);
        } catch (IOException e) {
            return new ErrorResponse(
                    "ApiClientException",
                    System.currentTimeMillis(),
                    Collections.singletonList("Api client exception occurred. Detail: " + getFeignExceptionContent(exception)),
                    Collections.emptyMap()
            );
        }
    }

    private String getMessage(FieldError error) {
        final String messageKey = error.getDefaultMessage();
        return messageSource.getMessage(messageKey, error.getArguments(), messageKey, new Locale("tr"));
    }
}
