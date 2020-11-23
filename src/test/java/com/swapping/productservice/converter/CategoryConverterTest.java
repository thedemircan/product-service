package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Category;
import com.swapping.productservice.domain.enums.CategoryName;
import com.swapping.productservice.model.response.CategoryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CategoryConverterTest {

    @InjectMocks
    private CategoryConverter categoryConverter;

    @Test
    public void it_should_to_dto() {
        // Given
        Category category = Category.builder()
                .description("description")
                .id(1)
                .name(CategoryName.ELECTRONIC)
                .build();

        // When
        CategoryDto categoryDto = categoryConverter.toDto(category);

        // Then
        assertThat(categoryDto).isEqualToComparingOnlyGivenFields(category);
    }
}