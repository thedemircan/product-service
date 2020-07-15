package com.swapping.productservice.converter;

import com.swapping.productservice.domain.Product;
import com.swapping.productservice.model.UpdateBasketItemDto;
import org.springframework.stereotype.Component;

@Component
public class UpdateBasketItemDtoConverter {

    public UpdateBasketItemDto convert(Product product) {
        return UpdateBasketItemDto.builder()
                .active(product.getActive())
                .deleted(product.getDeleted())
                .productId(product.getId())
                .price(product.getPrice())
                .build();
    }
}
