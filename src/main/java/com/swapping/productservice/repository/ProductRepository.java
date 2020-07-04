package com.swapping.productservice.repository;

import com.swapping.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCreatedUserIdAndActive(Integer userId, Boolean active);
}
