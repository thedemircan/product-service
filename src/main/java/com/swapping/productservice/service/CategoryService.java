package com.swapping.productservice.service;

import com.swapping.productservice.converter.CategoryConverter;
import com.swapping.productservice.model.constants.CacheConstant;
import com.swapping.productservice.model.response.CategoryDto;
import com.swapping.productservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Cacheable(value = CacheConstant.CATEGORY_CACHE, sync = true)
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream().map(categoryConverter::toDto).collect(Collectors.toList());
    }
}
