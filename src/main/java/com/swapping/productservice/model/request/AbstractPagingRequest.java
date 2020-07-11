package com.swapping.productservice.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractPagingRequest {
    private Integer page = 0;
    private Integer size = 20;
}
