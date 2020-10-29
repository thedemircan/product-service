package com.swapping.productservice.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class AbstractPagingRequest {

    private Integer page = 0;
    private Integer size = 20;
    private Sort.Direction sort = Sort.Direction.DESC;
}
