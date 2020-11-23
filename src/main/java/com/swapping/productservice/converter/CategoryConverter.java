package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Category;
import com.swapping.productservice.model.response.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .description(category.getDescription())
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
