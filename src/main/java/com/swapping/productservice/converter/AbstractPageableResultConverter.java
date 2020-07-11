package com.swapping.productservice.converter;

import com.swapping.productservice.model.request.AbstractPagingRequest;
import com.swapping.productservice.model.response.PageableResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public abstract class AbstractPageableResultConverter<T, V> implements BiFunction<Page<T>, AbstractPagingRequest, PageableResult<V>> {

    @Override
    public PageableResult<V> apply(Page<T> data, AbstractPagingRequest abstractPagingRequest) {
        PageableResult pageableResult = new PageableResult();
        pageableResult.setPage(abstractPagingRequest.getPage());
        pageableResult.setSize(abstractPagingRequest.getSize());
        pageableResult.setTotalPages(data.getTotalPages());
        pageableResult.setTotalElements(data.getTotalElements());
        pageableResult.setContent(convertContent(data));
        return pageableResult;
    }

    protected abstract List<V> convertContent(Page<T> data);
}
