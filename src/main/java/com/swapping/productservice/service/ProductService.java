package com.swapping.productservice.service;

import com.swapping.productservice.converter.ProductConverter;
import com.swapping.productservice.converter.ProductDtoConverter;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.exception.SwappingDomainNotFoundException;
import com.swapping.productservice.model.request.CreateProductRequest;
import com.swapping.productservice.model.request.DeleteProductRequest;
import com.swapping.productservice.model.request.ProductFilterRequest;
import com.swapping.productservice.model.request.UpdateProductRequest;
import com.swapping.productservice.model.response.ProductDto;
import com.swapping.productservice.repository.ProductRepository;
import com.swapping.productservice.repository.ProductSpecification;
import com.swapping.productservice.util.ValidateUtils;
import com.swapping.productservice.util.WordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final ProductDtoConverter productDtoConverter;

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
        ValidateUtils.assertAuthority(updateProductRequest.getUserId(), product.getCreatedUserId(), "update.product.authority");
        log.info("before update product: {} | updateProductRequest: {}", product, updateProductRequest);
        product.setName(WordUtil.toTitle(updateProductRequest.getName()));
        product.setDescription(updateProductRequest.getDescription().toLowerCase());
        product.setPrice(updateProductRequest.getPrice());
        //TODO: Ürünün fiyatı değiştiğini sepete bildir. Async
        product.setOriginalPrice(Objects.nonNull(updateProductRequest.getOriginalPrice()) ? updateProductRequest.getOriginalPrice() : null);
        product.setActive(false);
        product.setUpdatedUserId(updateProductRequest.getUserId());
        save(product);
    }

    public void deleteProduct(Integer id, DeleteProductRequest deleteProductRequest) {
        final Product product = findById(id);
        ValidateUtils.assertAuthority(deleteProductRequest.getUserId(), product.getCreatedUserId(), "delete.product.authority");
        log.info("product deleting: {}, userId: {}", id, deleteProductRequest.getUserId());
        product.setDeleted(true);
        product.setUpdatedUserId(deleteProductRequest.getUserId());
        //TODO: Ürünün silindiğini sepete bildir. Async
        save(product);
    }

    public ProductDto getProductDtoById(Integer id) {
        final Product product = findById(id);
        return productDtoConverter.apply(product);
    }

    public Page<Product> filter(ProductFilterRequest productFilterRequest) {
        return productRepository.findAll(ProductSpecification.getFilterQuery(productFilterRequest),
                                         PageRequest.of(productFilterRequest.getPage(), productFilterRequest.getSize()));
    }
}
