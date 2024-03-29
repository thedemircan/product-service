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
import com.swapping.productservice.repository.ProductSpecification;
import com.swapping.productservice.util.ValidateUtils;
import com.swapping.productservice.util.WordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String SORT_BY_ID = "id";

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new SwappingDomainNotFoundException("product.not.found"));
    }

    public void createProduct(CreateProductRequest createProductRequest) {
        final Product product = productConverter.toEntity(createProductRequest);
        save(product);
    }

    public void updateProduct(Integer id, UpdateProductRequest updateProductRequest) {
        final Product product = findById(id);
        ValidateUtils.assertAuthority(updateProductRequest.getUserId(), product.getCreatedUserId(), "update.product.authority");
        log.info("before update product: {} | updateProductRequest: {}", product, updateProductRequest);
        product.setName(WordUtil.toCapitalizeFully(updateProductRequest.getName()));
        product.setDescription(updateProductRequest.getDescription().toLowerCase());
        product.setPrice(updateProductRequest.getPrice());
        product.setOriginalPrice(updateProductRequest.getOriginalPrice());
        product.setActive(Boolean.FALSE);
        product.setUpdatedUserId(updateProductRequest.getUserId());
        save(product);
    }

    public void deleteProduct(Integer id, DeleteProductRequest deleteProductRequest) {
        final Product product = findById(id);
        ValidateUtils.assertAuthority(deleteProductRequest.getUserId(), product.getCreatedUserId(), "delete.product.authority");
        log.info("product deleting: {}, userId: {}", id, deleteProductRequest.getUserId());
        product.setDeleted(Boolean.TRUE);
        product.setActive(Boolean.FALSE);
        product.setUpdatedUserId(deleteProductRequest.getUserId());
        save(product);
    }

    public ProductDto getProductDtoById(Integer id) {
        final Product product = findById(id);
        return productConverter.toDto(product);
    }

    public PagingResult<ProductDto> filter(ProductFilterRequest productFilterRequest) {
        final Pageable pageable = PageRequest.of(productFilterRequest.getPage(),
                                                 productFilterRequest.getSize(),
                                                 productFilterRequest.getSort(),
                                                 SORT_BY_ID);
        final Page<Product> products = productRepository.findAll(ProductSpecification.getFilterQuery(productFilterRequest), pageable);
        return new PagingResult<>(products.map(productConverter::toDto).getContent(),
                                  products.getTotalPages(),
                                  products.getTotalElements(),
                                  products.getSize(),
                                  products.getNumber());
    }
}
