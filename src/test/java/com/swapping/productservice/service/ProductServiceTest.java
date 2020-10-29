package com.swapping.productservice.service;

import com.swapping.productservice.converter.ProductConverter;
import com.swapping.productservice.converter.ProductDtoConverter;
import com.swapping.productservice.converter.UpdateBasketItemDtoConverter;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.model.UpdateBasketItemDto;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.DeleteProductRequest;
import com.swapping.productservice.model.request.ProductFilterRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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

    @Mock
    private UpdateBasketItemDtoConverter updateBasketItemDtoConverter;

    @Mock
    private RabbitTemplate rabbitTemplate;

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

        UpdateBasketItemDto updateBasketItemDto = UpdateBasketItemDto.builder().build();
        when(updateBasketItemDtoConverter.convert(product)).thenReturn(updateBasketItemDto);

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
        verify(updateBasketItemDtoConverter).convert(product);
        verify(rabbitTemplate).convertAndSend("update.basket.item", "", updateBasketItemDto);
    }

    @Test
    public void it_should_delete_product() {
        // Given
        DeleteProductRequest deleteProductRequest = DeleteProductRequest.builder().userId(34).build();
        Product product = Product.builder().createdUserId(34).build();

        when(productRepository.findById(79)).thenReturn(Optional.of(product));

        UpdateBasketItemDto updateBasketItemDto = UpdateBasketItemDto.builder().build();
        when(updateBasketItemDtoConverter.convert(product)).thenReturn(updateBasketItemDto);

        // When
        productService.deleteProduct(79, deleteProductRequest);

        // Then
        verify(productRepository).findById(79);
        verify(productRepository).save(argThat(saveProduct -> saveProduct.getDeleted().equals(true) && saveProduct.getUpdatedUserId().equals(34)));
        verify(updateBasketItemDtoConverter).convert(product);
        verify(rabbitTemplate).convertAndSend("update.basket.item", "", updateBasketItemDto);
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
    public void it_should_filter() {
        //Given
        ProductFilterRequest request = new ProductFilterRequest();
        request.setPage(0);
        request.setSize(20);
        request.setSort(Sort.Direction.DESC);

        Product product1 = Product.builder().build();
        Product product2 = Product.builder().build();
        Page page = new PageImpl(Arrays.asList(product1, product2));

        Pageable pageable = PageRequest.of(0, 20, Sort.Direction.DESC, "id");
        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        //When
        Page<Product> actualPage = productService.filter(request);

        //Then
        assertThat(actualPage).isEqualTo(page);
    }
}