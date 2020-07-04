package com.swapping.productservice.util;

import com.swapping.productservice.exception.SwappingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidateUtils {

    public static void assertAuthority(Integer requestUserId, Integer productUserId, String messageKey) {
        if (!productUserId.equals(requestUserId)) {
            throw new SwappingException(messageKey, HttpStatus.FORBIDDEN);
        }
    }
}
