package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.response.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductDtoPageableResultConverter extends AbstractPageableResultConverter<Product, ProductDto> {

    private final ProductDtoConverter productDtoConverter;

    @Override
    protected List<ProductDto> convertContent(Page<Product> data) {
        return data.get().map(productDtoConverter::apply).collect(Collectors.toList());
    }
}
