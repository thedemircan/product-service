package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Category;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.domain.enums.CategoryName;
import com.swapping.productservice.model.response.ProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductDtoConverterTest {

    @InjectMocks
    private ProductDtoConverter productDtoConverter;

    @Test
    public void it_should_apply_to_list() {
        // Given
        Category category = Category.builder().name(CategoryName.ELECTRONIC).build();

        Product product1 = Product.builder()
                .originalPrice(BigDecimal.valueOf(100))
                .category(category)
                .description("Description")
                .name("Bilgisayar")
                .active(true)
                .createdDate(LocalDateTime.of(2020, 1, 1, 1, 1))
                .build();

        Product product2 = Product.builder()
                .category(category)
                .description("Description")
                .name("Telefon")
                .active(false)
                .createdDate(LocalDateTime.of(2021, 1, 1, 1, 1))

                .build();

        List<Product> products = Arrays.asList(product1, product2);

        // When
        List<ProductDto> productDtoList = productDtoConverter.applyToList(products);

        // Then
        assertThat(productDtoList.get(0).getCategory()).isEqualTo(CategoryName.ELECTRONIC);
        assertThat(productDtoList.get(0).getOriginalPrice()).isNull();
        assertThat(productDtoList.get(0).getDescription()).isEqualTo("Description");
        assertThat(productDtoList.get(0).getName()).isEqualTo("Telefon");
        assertThat(productDtoList.get(0).getActive()).isFalse();

        assertThat(productDtoList.get(1).getCategory()).isEqualTo(CategoryName.ELECTRONIC);
        assertThat(productDtoList.get(1).getOriginalPrice()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(productDtoList.get(1).getDescription()).isEqualTo("Description");
        assertThat(productDtoList.get(1).getName()).isEqualTo("Bilgisayar");
        assertThat(productDtoList.get(1).getActive()).isTrue();
    }
}