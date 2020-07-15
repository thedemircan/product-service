package com.swapping.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBasketItemDto {

    private Integer productId;
    private BigDecimal price;
    private Boolean deleted;
    private Boolean active;
}
