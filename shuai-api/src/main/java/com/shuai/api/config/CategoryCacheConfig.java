package com.shuai.api.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.shuai.api.cache.CategoryCache;
import com.shuai.api.client.course.CategoryClient;
import com.shuai.api.dto.course.CategoryBasicDTO;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.Map;

public class CategoryCacheConfig {

    /**
     * 课程分类的caffeine缓存
     * initialCapacity(1) : 个参数设置了缓存初始化时的初始容量为 1。这意味着缓存一开始只会分配足够的空间来存储 1 个条目。
     *                      随着缓存中条目的增加，Caffeine 会动态地扩展容量，直到达到 maximumSize
     * maximumSize(10_000) : 这个参数设置了缓存的最大容量为 10,000 条记录。当缓存中的条目超过这个数量时，
     *                       Caffeine 会根据其内部的淘汰策略（通常是最近最少使用 LRU）来移除旧的条目，以腾出空间给新的条目。
     * expireAfterWrite(Duration.ofMinutes(30)) ： 有效期是30分钟
     */
    @Bean
    public Cache<String, Map<Long, CategoryBasicDTO>> categoryCaches() {
        return Caffeine.newBuilder()
                .initialCapacity(1)  // 容量限制
                .maximumSize(10_000)  // 最大内存限制
                .expireAfterWrite(Duration.ofMinutes(30))  // 有效期
                .build();
    }

    /**
     * 课程分类的缓存工具类
     */
    @Bean
    public CategoryCache categoryCache(
            Cache<String, Map<Long, CategoryBasicDTO>> categoryCaches, CategoryClient categoryClient
            ) {
        return new CategoryCache(categoryCaches,categoryClient);
    }
}
