package com.swapping.productservice.util;

import com.swapping.productservice.exception.SwappingException;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ValidateUtilsTest {

    @Test
    public void it_should_assert_authority() {
        // When
        Throwable throwable = catchThrowable(() -> ValidateUtils.assertAuthority(34, 79, "message.key"));

        // Then
        SwappingException swappingException = (SwappingException) throwable;
        assertThat(swappingException.getKey()).isEqualTo("message.key");
        assertThat(swappingException.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}