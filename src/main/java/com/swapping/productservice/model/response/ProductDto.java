package com.swapping.productservice.model.response;

import com.swapping.productservice.domain.enums.CategoryName;
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
public class ProductDto {

    private String name;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private CategoryName category;
    private String description;
    private Boolean active;
}
