package com.swapping.productservice.model.request;

import com.swapping.productservice.domain.enums.CategoryName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest extends AbstractPagingRequest {

    private Integer userId;

    @Builder.Default
    private boolean active = true;

    private CategoryName category;
}
