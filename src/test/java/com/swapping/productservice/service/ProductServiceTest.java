package com.swapping.productservice.service;

import com.swapping.productservice.converter.ProductConverter;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.DeleteProductRequest;
import com.swapping.productservice.model.request.ProductFilterRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.model.response.PagingResult;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
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
        when(productConverter.toEntity(createProductRequest)).thenReturn(product);

        // When
        productService.createProduct(createProductRequest);

        // Then
        verify(productConverter).toEntity(createProductRequest);
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
        when(productConverter.toDto(product)).thenReturn(productDto);

        // When
        ProductDto actualProductDto = productService.getProductDtoById(79);

        // Then
        verify(productRepository).findById(79);
        verify(productConverter).toDto(product);
        assertThat(actualProductDto).isEqualTo(productDto);
    }

    @Test
    public void it_should_filter() {
        //Given
        ProductFilterRequest productFilterRequest = new ProductFilterRequest();
        productFilterRequest.setPage(1);
        productFilterRequest.setSize(20);
        productFilterRequest.setSort(Sort.Direction.ASC);

        Product product = Product.builder().build();
        Page<Product> productPage = new PageImpl(Collections.singletonList(product));
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        ProductDto productDto = ProductDto.builder().build();

        when(productRepository.findAll(any(Specification.class), pageableArgumentCaptor.capture())).thenReturn(productPage);
        when(productConverter.toDto(product)).thenReturn(productDto);

        // When
        PagingResult<ProductDto> pageableProducts = productService.filter(productFilterRequest);

        // Then
        verify(productConverter).toDto(product);
        verify(productRepository).findAll(any(Specification.class), pageableArgumentCaptor.capture());
        assertThat(pageableProducts.getContent()).containsExactly(productDto);
        assertThat(pageableArgumentCaptor.getValue().getPageNumber()).isEqualTo(1);
        assertThat(pageableArgumentCaptor.getValue().getPageSize()).isEqualTo(20);
        assertThat(pageableArgumentCaptor.getValue().isPaged()).isTrue();
        assertThat(pageableArgumentCaptor.getValue().getSort().isSorted()).isTrue();
    }
}