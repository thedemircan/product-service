package com.swapping.productservice.model.error;

import com.swapping.productservice.util.Clock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 8313215963092335474L;

    private String exception;

    @Builder.Default
    private Long timestamp = Clock.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    private String error;

    @Singular
    private Map<String, String> fieldErrors = new HashMap<>();
}
