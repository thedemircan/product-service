package com.swapping.productservice.service;

import com.swapping.productservice.converter.CategoryConverter;
import com.swapping.productservice.domain.Category;
import com.swapping.productservice.model.response.CategoryDto;
import com.swapping.productservice.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryConverter categoryConverter;

    @Test
    public void it_should_get_all() {
        // Given
        Category category = Category.builder().build();
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        CategoryDto categoryDto = CategoryDto.builder().build();
        when(categoryConverter.toDto(category)).thenReturn(categoryDto);

        // When
        List<CategoryDto> categoryDtoList = categoryService.getAll();

        // Then
        assertThat(categoryDtoList).contains(categoryDto);
    }
}