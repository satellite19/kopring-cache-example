package com.example.kopringcacheexample.infrastructure.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/**
 * 캐시 설정
 */
@Configuration
@EnableCaching
class CacheConfig {

    /**
     * Caffeine 캐시 매니저 설정
     */
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats()
        )
        cacheManager.setCacheNames(setOf("encryptionKeys"))
        return cacheManager
    }
}
