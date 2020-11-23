package com.swapping.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapping.productservice.domain.enums.CategoryName;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.DeleteProductRequest;
import com.swapping.productservice.model.request.ProductFilterRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.model.response.PagingResult;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void it_should_create_product() throws Exception {
        // Given
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .description("description")
                .name("product name")
                .price(BigDecimal.valueOf(20))
                .category(CategoryName.ELECTRONIC)
                .userId(2)
                .build();

        ArgumentCaptor<CreateProductRequest> createProductRequestArgumentCaptor = ArgumentCaptor.forClass(CreateProductRequest.class);

        // When
        ResultActions resultActions = mockMvc.perform(post("/products")
                                                              .content(new ObjectMapper().writeValueAsString(createProductRequest))
                                                              .contentType(MediaType.APPLICATION_JSON));

        // Then
        verify(productService).createProduct(createProductRequestArgumentCaptor.capture());
        assertThat(createProductRequestArgumentCaptor.getValue()).isEqualToComparingFieldByField(createProductRequest);
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void it_should_update_product() throws Exception {
        // Given
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .userId(100)
                .description("description")
                .name("product name")
                .price(BigDecimal.valueOf(20))
                .originalPrice(BigDecimal.TEN)
                .build();

        ArgumentCaptor<UpdateProductRequest> updateProductRequestArgumentCaptor = ArgumentCaptor.forClass(UpdateProductRequest.class);

        // When
        ResultActions resultActions = mockMvc.perform(put("/products/1")
                                                              .content(new ObjectMapper().writeValueAsString(updateProductRequest))
                                                              .contentType(MediaType.APPLICATION_JSON));

        // Then
        verify(productService).updateProduct(eq(1), updateProductRequestArgumentCaptor.capture());
        assertThat(updateProductRequestArgumentCaptor.getValue()).isEqualToComparingFieldByField(updateProductRequest);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void it_should_delete_product() throws Exception {
        // Given
        DeleteProductRequest deleteProductRequest = DeleteProductRequest.builder().userId(100).build();

        ArgumentCaptor<DeleteProductRequest> deleteProductRequestArgumentCaptor = ArgumentCaptor.forClass(DeleteProductRequest.class);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/products/1")
                                                              .content(new ObjectMapper().writeValueAsString(deleteProductRequest))
                                                              .contentType(MediaType.APPLICATION_JSON));

        // Then
        verify(productService).deleteProduct(eq(1), deleteProductRequestArgumentCaptor.capture());
        assertThat(deleteProductRequestArgumentCaptor.getValue()).isEqualToComparingFieldByField(deleteProductRequest);
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void it_should_get_product_dto_by_id() throws Exception {
        // Given
        ProductDto productDto = ProductDto.builder()
                .name("ProductName")
                .originalPrice(BigDecimal.TEN)
                .price(BigDecimal.ONE)
                .description("Desc")
                .category(CategoryName.ELECTRONIC)
                .active(true)
                .build();

        when(productService.getProductDtoById(79)).thenReturn(productDto);

        // When
        ResultActions resultActions = mockMvc.perform(get("/products/79"));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ProductName"))
                .andExpect(jsonPath("$.originalPrice").value("10"))
                .andExpect(jsonPath("$.price").value("1"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.active").value("true"))
                .andExpect(jsonPath("$.category").value("ELECTRONIC"));
    }

    @Test
    public void it_should_filter() throws Exception {
        //Given
        ArgumentCaptor<ProductFilterRequest> requestArgumentCaptor = ArgumentCaptor.forClass(ProductFilterRequest.class);

        ProductDto productDto1 = ProductDto.builder().build();
        ProductDto productDto2 = ProductDto.builder().build();
        List<ProductDto> content = Arrays.asList(productDto1, productDto2);
        PagingResult<ProductDto> pagingResult = new PagingResult<>(content, 27, 0, 2, 1);
        when(productService.filter(requestArgumentCaptor.capture())).thenReturn(pagingResult);


        //When
        ResultActions resultActions = mockMvc.perform(get("/products?page=0&size=2").contentType(MediaType.APPLICATION_JSON));

        //Then
        resultActions.andExpect(status().isOk());
        verify(productService).filter(requestArgumentCaptor.capture());
    }
}