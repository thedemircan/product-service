package com.swapping.productservice.model.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PageableResult<T> {

    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private long totalElements;
    private int totalPages;
    private Integer page;
    private Integer size;
    private Collection<T> content;


    public PageableResult() {
    }

    public PageableResult(long totalElements, Integer page, Integer size, Collection<T> content) {
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
        this.content = content;
        this.totalPages = calculateTotalPageCount();
    }

    public static PageableResult getEmptyPageableResult() {
        return new PageableResult(ZERO, ZERO, ZERO, Collections.emptyList());
    }

    public static <K> PageableResult<K> getSingletonPageableResult(K content) {
        return new PageableResult(ONE, ZERO, ONE, Collections.singleton(content));
    }

    public static <K> PageableResult<K> getMultiplePageableResult(K... contents) {
        Collection<K> collection = new ArrayList<>();
        Collections.addAll(collection, contents);
        return new PageableResult(collection.size(), ZERO, collection.size(), collection);
    }

    private int calculateTotalPageCount() {
        if (ZERO.equals(size)) {
            return ZERO;
        }
        int fullPageCount = (int) (totalElements / size);
        if (totalElements % size == ZERO) {
            return fullPageCount;
        }
        return fullPageCount + ONE;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Collection<T> getContent() {
        return content;
    }

    public void setContent(Collection<T> content) {
        this.content = content;
    }
}
