package com.swapping.productservice.repository;

import com.swapping.productservice.domain.Category;
import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.request.ProductFilterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ProductSpecificationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void it_should_filter_by_active() {
        // Given
        Category category = Category.builder().build();
        testEntityManager.persistAndFlush(category);

        Product productActive = Product.builder()
                .name("ProductName")
                .price(BigDecimal.TEN)
                .description("Description")
                .createdUserId(79)
                .active(true)
                .categoryId(category.getId())
                .build();

        Product productUnActive = Product.builder()
                .name("ProductName")
                .price(BigDecimal.TEN)
                .description("Description")
                .createdUserId(79)
                .active(false)
                .categoryId(category.getId())
                .build();

        productRepository.save(productActive);
        productRepository.save(productUnActive);

        ProductFilterRequest productFilterRequest = ProductFilterRequest.builder().active(false).build();
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Product> productPage = productRepository.findAll(ProductSpecification.getFilterQuery(productFilterRequest), pageable);

        // Then
        assertThat(productPage).containsOnly(productUnActive);
    }

    @Test
    public void it_should_filter_by_user_id() {
        // Given
        Category category = Category.builder().build();
        testEntityManager.persistAndFlush(category);

        Product product1 = Product.builder()
                .name("ProductName")
                .price(BigDecimal.TEN)
                .description("Description")
                .createdUserId(79)
                .active(true)
                .categoryId(category.getId())
                .build();

        Product product2 = Product.builder()
                .name("ProductName")
                .price(BigDecimal.TEN)
                .description("Description")
                .createdUserId(79)
                .active(true)
                .categoryId(category.getId())
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        ProductFilterRequest productFilterRequest = ProductFilterRequest.builder().userId(79).build();
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Product> productPage = productRepository.findAll(ProductSpecification.getFilterQuery(productFilterRequest), pageable);

        // Then
        assertThat(productPage).containsExactlyInAnyOrder(product1, product2);
    }
}