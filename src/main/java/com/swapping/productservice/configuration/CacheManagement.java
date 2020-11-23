package com.swapping.productservice.configuration;

import com.swapping.productservice.model.constants.CacheConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CacheManagement {

    private static final int A_HOUR = 3600000;

    private final CacheManager cacheManager;

    @Scheduled(fixedDelay = A_HOUR * 2)
    public void clearCacheTwoHours() {
        clearCache(CacheConstant.CATEGORY_CACHE);
    }

    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (Objects.nonNull(cache)) {
            cache.clear();
        }
    }
}
