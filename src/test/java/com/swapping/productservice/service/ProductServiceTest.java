package com.swapping.productservice.service;

import com.swapping.productservice.converter.ProductConverter;
import com.swapping.productservice.converter.ProductDtoConverter;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.DeleteProductRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.argThat;
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

    @Mock
    private ProductDtoConverter productDtoConverter;

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
    public void it_should_throw_exception_when_product_not_found() {
        // Given
        when(productRepository.findById(34)).thenReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> productService.findById(34));

        // Then
        verify(productRepository).findById(34);
        SwappingDomainNotFoundException notFoundException = (SwappingDomainNotFoundException) throwable;
        assertThat(notFoundException.getKey()).isEqualTo("product.not.found");
        assertThat(notFoundException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
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
                .userId(34)
                .build();

        Product product = Product.builder().createdUserId(34).build();
        when(productRepository.findById(79)).thenReturn(Optional.of(product));

        // When
        productService.updateProduct(79, updateProductRequest);

        // Then
        verify(productRepository).findById(79);
        assertThat(product.getOriginalPrice()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(product.getPrice()).isEqualTo(BigDecimal.TEN);
        assertThat(product.getName()).isEqualTo("Product Name");
        assertThat(product.getDescription()).isEqualTo("description");
        assertThat(product.getActive()).isFalse();
        assertThat(product.getUpdatedUserId()).isEqualTo(34);
        verify(productRepository).save(product);
    }

    @Test
    public void it_should_delete_product() {
        // Given
        DeleteProductRequest deleteProductRequest = DeleteProductRequest.builder().userId(34).build();
        Product product = Product.builder().createdUserId(34).build();

        when(productRepository.findById(79)).thenReturn(Optional.of(product));

        // When
        productService.deleteProduct(79, deleteProductRequest);

        // Then
        verify(productRepository).findById(79);
        verify(productRepository).save(argThat(saveProduct -> saveProduct.getDeleted().equals(true) && saveProduct.getUpdatedUserId().equals(34)));
    }

    @Test
    public void it_should_get_product_dto_by_id() {
        // Given
        Product product = Product.builder().build();
        when(productRepository.findById(79)).thenReturn(Optional.of(product));

        ProductDto productDto = ProductDto.builder().build();
        when(productDtoConverter.apply(product)).thenReturn(productDto);

        // When
        ProductDto actualProductDto = productService.getProductDtoById(79);

        // Then
        verify(productRepository).findById(79);
        verify(productDtoConverter).apply(product);
        assertThat(actualProductDto).isEqualTo(productDto);
    }

    @Test
    public void it_should_get_product_dto_list_by_user_id() {
        // Given
        Product product1 = Product.builder().build();
        Product product2 = Product.builder().build();
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findByCreatedUserIdAndActive(79, true)).thenReturn(products);

        ProductDto productDto1 = ProductDto.builder().build();
        ProductDto productDto2 = ProductDto.builder().build();
        List<ProductDto> productDtoList = Arrays.asList(productDto1, productDto2);

        when(productDtoConverter.applyToList(products)).thenReturn(productDtoList);

        // When
        List<ProductDto> actualProductDtoList = productService.getProductDtoListByUserId(79, true);

        // Then
        verify(productRepository).findByCreatedUserIdAndActive(79, true);
        verify(productDtoConverter).applyToList(products);
        assertThat(actualProductDtoList).containsExactlyInAnyOrder(productDto1, productDto2);
    }
}