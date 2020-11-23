package com.swapping.productservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryName {

    ELECTRONIC(1, "Elektronik"),
    COSMETIC(2, "Kozmetik");

    private final Integer id;
    private final String description;
}
