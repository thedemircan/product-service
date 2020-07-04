package com.swapping.productservice.repository;

import com.swapping.productservice.domain.Category;
import com.swapping.productservice.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void it_should_find_by_created_user_id_and_active() {
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

        List<Product> productList = Arrays.asList(productActive, productUnActive);
        productRepository.saveAll(productList);

        // When
        List<Product> products = productRepository.findByCreatedUserIdAndActive(79, true);

        // Then
        assertThat(products).containsOnly(productActive);
    }
}