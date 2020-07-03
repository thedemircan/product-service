package com.swapping.productservice.service;

import com.swapping.productservice.converter.ProductConverter;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductConverter productConverter;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void it_should_find_by_id() {
        // Given
        Product product = Product.builder().build();
        when(productRepository.findById(34)).thenReturn(Optional.of(product));

        // When
        Product actualProduct = productService.findById(34);

        // Then
        verify(productRepository).findById(34);
        assertThat(product).isEqualTo(actualProduct);
    }

    @Test
    public void it_should_save() {
        // Given
        Product product = Product.builder().build();

        // When
        productService.save(product);

        // Then
        verify(productRepository).save(product);
    }

    @Test
    public void it_should_create_product() {
        // Given
        CreateProductRequest createProductRequest = CreateProductRequest.builder().build();

        Product product = Product.builder().build();
        when(productConverter.apply(createProductRequest)).thenReturn(product);

        // When
        productService.createProduct(createProductRequest);

        // Then
        verify(productConverter).apply(createProductRequest);
        verify(productRepository).save(product);
    }

    @Test
    public void it_should_update_product() {
        // Given
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .description("descRiptioN")
                .name("product name")
                .originalPrice(BigDecimal.valueOf(20))
                .price(BigDecimal.TEN)
                .build();

        Product product = Product.builder().build();
        when(productRepository.findById(79)).thenReturn(Optional.of(product));

        // When
        productService.updateProduct(79, updateProductRequest);

        // Then
        verify(productRepository).findById(79);
        assertThat(product.getOriginalPrice()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product.getPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(product.getName()).isEqualTo("Product Name");
        assertThat(product.getDescription()).isEqualTo("description");
        verify(productRepository).save(product);
    }
}