package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.request.ProductFilterRequest;
import com.swapping.productservice.model.response.PageableResult;
import com.swapping.productservice.model.response.ProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDtoPageableResultConverterTest {

    @InjectMocks
    private ProductDtoPageableResultConverter productDtoPageableResultConverter;

    @Mock
    private ProductDtoConverter productDtoConverter;

    @Test
    public void it_should_convert() {
        //Given
        ProductFilterRequest productFilterRequest = ProductFilterRequest.builder().build();
        productFilterRequest.setPage(0);
        productFilterRequest.setSize(10);

        Product product1 = Product.builder().build();
        Product product2 = Product.builder().build();
        Page<Product> data = new PageImpl<>(Arrays.asList(product1, product2));

        ProductDto productDto1 = ProductDto.builder().build();
        ProductDto productDto2 = ProductDto.builder().build();
        when(productDtoConverter.apply(product1)).thenReturn(productDto1);
        when(productDtoConverter.apply(product2)).thenReturn(productDto2);

        //When
        PageableResult<ProductDto> actualPageableResult = productDtoPageableResultConverter.apply(data, productFilterRequest);

        //Then
        assertThat(actualPageableResult.getContent()).containsExactlyInAnyOrder(productDto1, productDto2);
    }
}