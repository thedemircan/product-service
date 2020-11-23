package com.swapping.productservice.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CacheManagementTest {

    @InjectMocks
    private CacheManagement cacheManagement;

    @Mock
    private CacheManager cacheManager;

    @Test
    public void it_should_clear_cache() {
        // Given
        Cache mockCache = mock(Cache.class);
        when(cacheManager.getCache("cacheName")).thenReturn(mockCache);

        // When
        cacheManagement.clearCache("cacheName");

        // Then
        verify(mockCache).clear();
    }
}