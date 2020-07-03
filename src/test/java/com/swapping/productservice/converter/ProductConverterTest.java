package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.domain.enums.CategoryName;
import com.swapping.productservice.model.request.CreateProductRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductConverterTest {

    @InjectMocks
    private ProductConverter productConverter;

    @Test
    public void it_should_apply() {
        // Given
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .description("DeScription")
                .name("product name")
                .category(CategoryName.ELECTRONIC)
                .price(BigDecimal.valueOf(20))
                .userId(2)
                .build();

        // When
        Product product = productConverter.apply(createProductRequest);

        // Then
        assertThat(product.getCategoryId()).isEqualTo(1);
        assertThat(product.getDescription()).isEqualTo("description");
        assertThat(product.getName()).isEqualTo("Product Name");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product.getCreatedUserId()).isEqualTo(2);
        assertThat(product.getActive()).isFalse();
    }
}