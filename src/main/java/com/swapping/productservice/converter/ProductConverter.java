package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.util.WordUtil;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    public Product toEntity(CreateProductRequest createProductRequest) {
        return Product.builder()
                .name(WordUtil.toCapitalizeFully(createProductRequest.getName()))
                .price(createProductRequest.getPrice())
                .originalPrice(createProductRequest.getOriginalPrice())
                .categoryId(createProductRequest.getCategory().getId())
                .description(createProductRequest.getDescription().toLowerCase())
                .createdUserId(createProductRequest.getUserId())
                .active(Boolean.FALSE)
                .build();
    }

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .userId(product.getCreatedUserId())
                .category(product.getCategory().getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .name(product.getName())
                .active(product.getActive())
                .build();
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream().sorted(Comparator.comparing(Product::getCreatedDate).reversed()).map(this::toDto).collect(Collectors.toList());
    }
}
