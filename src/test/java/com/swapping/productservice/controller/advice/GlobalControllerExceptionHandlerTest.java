package com.swapping.productservice.controller.advice;

import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.exception.SwappingException;
import com.swapping.productservice.model.error.ErrorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalControllerExceptionHandlerTest {

    @InjectMocks
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @Mock
    private MessageSource messageSource;

    @Test
    public void it_should_resolve_swappingException() {
        // Given
        when(messageSource.getMessage(eq("key"), any(), any())).thenReturn("key-message");
        SwappingException exception = new SwappingDomainNotFoundException("key");

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalControllerExceptionHandler.handleSwappingException(exception);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getErrors()).containsExactly("key-message");
    }


    @Test
    public void it_should_resolve_methodArgumentNotValidException() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(mock(MethodParameter.class),
                                                                                                              bindingResult) {
            @Override
            public String getMessage() {
                return "method-argument-not-valid-exception";
            }
        };

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalControllerExceptionHandler.handleMethodArgumentNotValidException(
                methodArgumentNotValidException);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
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
        assertThat(responseEntity.getBody().getErrors()).containsExactly("bulunamadi");
    }
}