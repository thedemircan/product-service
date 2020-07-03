package com.swapping.productservice.model.request;

import com.swapping.productservice.domain.enums.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotNull(message = "userId.notNull")
    private Integer userId;

    @NotBlank(message = "productName.notBlank")
    private String name;

    @NotNull(message = "productPrice.notNull")
    private BigDecimal price;

    private BigDecimal originalPrice;

    @NotBlank(message = "productDescription.notBlank")
    private String description;

    @NotNull(message = "productCategory.notNull")
    private CategoryName category;
}
