package com.swapping.productservice.controller;

import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.DeleteProductRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody @Valid CreateProductRequest createProductRequest) {
        productService.createProduct(createProductRequest);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Integer id, @RequestBody @Valid UpdateProductRequest updateProductRequest) {
        productService.updateProduct(id, updateProductRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id, @RequestBody @Valid DeleteProductRequest deleteProductRequest) {
        productService.deleteProduct(id, deleteProductRequest);
    }

    @GetMapping("/{id}")
    public ProductDto getProductDtoById(@PathVariable Integer id) {
        return productService.getProductDtoById(id);
    }

    @GetMapping
    public List<ProductDto> getProductDtoListByUserId(@RequestParam @NotNull Integer userId,
                                                      @RequestParam @NotNull Boolean active) {
        return productService.getProductDtoListByUserId(userId, active);
    }
}
