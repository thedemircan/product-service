package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.util.WordUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class ProductConverter implements Function<CreateProductRequest, Product> {

    @Override
    public Product apply(CreateProductRequest createProductRequest) {
        return Product.builder()
                .name(WordUtil.toTitle(createProductRequest.getName()))
                .price(createProductRequest.getPrice())
                .originalPrice(Objects.nonNull(createProductRequest.getOriginalPrice()) ? createProductRequest.getOriginalPrice() : null)
                .categoryId(createProductRequest.getCategory().getId())
                .description(createProductRequest.getDescription().toLowerCase())
                .createdUserId(createProductRequest.getUserId())
                .active(Boolean.FALSE)
                .build();
    }
}
