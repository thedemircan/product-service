package com.swapping.productservice.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.exception.SwappingException;
import com.swapping.productservice.model.error.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    @InjectMocks
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void it_should_resolve_swappingException() {
        // Given
        when(messageSource.getMessage(eq("key"), any(), any())).thenReturn("key-message");
        SwappingException exception = new SwappingDomainNotFoundException("key");

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalControllerExceptionHandler.handleSwappingException(exception);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getError()).isEqualTo("key-message");
    }

    @Test
    public void it_should_resolve_swappingDomainNotFoundException() {
        // Given
        SwappingDomainNotFoundException notFoundException = new SwappingDomainNotFoundException("not.found");
        when(messageSource.getMessage(eq("not.found"), any(), any())).thenReturn("bulunamadi");

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalControllerExceptionHandler.handleSwappingDomainNotFoundException(notFoundException);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getError()).isEqualTo("bulunamadi");
    }
}