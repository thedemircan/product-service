package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.response.ProductDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductDtoConverter {

    public ProductDto apply(Product product) {
        return ProductDto.builder()
                .category(product.getCategory().getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .originalPrice(Objects.nonNull(product.getOriginalPrice()) ? product.getOriginalPrice() : null)
                .name(product.getName())
                .active(product.getActive())
                .build();
    }

    public List<ProductDto> applyToList(List<Product> products) {
        return products.stream().sorted(Comparator.comparing(Product::getCreatedDate).reversed()).map(this::apply).collect(Collectors.toList());
    }
}
