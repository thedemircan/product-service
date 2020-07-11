package com.swapping.productservice.repository;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.request.ProductFilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductSpecification {

    private static final String CATEGORY_ID = "categoryId";
    private static final String ACTIVE = "active";
    private static final String IS_DELETED = "deleted";
    private static final String USER_ID = "createdUserId";


    public static Specification<Product> getFilterQuery(ProductFilterRequest productFilterRequest) {

        return (root, query, cb) -> {
            Predicate conjunction = cb.conjunction();
            conjunction = cb.and(conjunction, cb.and(cb.equal(root.get(IS_DELETED), false)));

            conjunction = cb.and(conjunction, cb.and(cb.equal(root.get(ACTIVE), productFilterRequest.isActive())));

            if (Objects.nonNull(productFilterRequest.getUserId())) {
                conjunction = cb.and(conjunction, cb.and(cb.equal(root.get(USER_ID), productFilterRequest.getUserId())));
            }

            if (Objects.nonNull(productFilterRequest.getCategory())) {
                conjunction = cb.and(conjunction, cb.and(cb.equal(root.get(CATEGORY_ID), productFilterRequest.getCategory().getId())));
            }

            query.distinct(true);
            return conjunction;
        };
    }

}
