package com.example.kopringcacheexample.infrastructure.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfig {

    /**
     * Caffeine 캐시 매니저 설정
     * - maximumSize로 OOM을 방지함
     * - 모든 캐시 이름을 미리 등록
     */
    @Bean
    fun cacheManager(): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        
        // 모든 캐시에 대해 동일한 Caffeine 설정 적용
        val caffeineBuilder = Caffeine.newBuilder()
            .maximumSize(1000) // 최대 캐시 항목 수 (초과 시 LRU 방식으로 제거)
        
        caffeineCacheManager.setCaffeine(caffeineBuilder)
        
        // 모든 캐시 이름 사전 등록
        caffeineCacheManager.setCacheNames(CacheNames.getAllCacheNames())
        
        return caffeineCacheManager
    }
}
