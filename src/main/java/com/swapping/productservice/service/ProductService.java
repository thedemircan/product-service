package com.swapping.productservice.service;

import com.swapping.productservice.converter.ProductConverter;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.repository.ProductRepository;
import com.swapping.productservice.util.WordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new SwappingDomainNotFoundException("product.not.found"));
    }

    public void createProduct(CreateProductRequest createProductRequest) {
        final Product product = productConverter.apply(createProductRequest);
        save(product);
    }

    public void updateProduct(Integer id, UpdateProductRequest updateProductRequest) {
        final Product product = findById(id);
        product.setName(WordUtil.toTitle(updateProductRequest.getName()));
        product.setDescription(updateProductRequest.getDescription().toLowerCase());
        product.setPrice(updateProductRequest.getPrice());
        product.setOriginalPrice(Objects.nonNull(updateProductRequest.getOriginalPrice()) ? updateProductRequest.getOriginalPrice() : null);
        save(product);
    }
}
