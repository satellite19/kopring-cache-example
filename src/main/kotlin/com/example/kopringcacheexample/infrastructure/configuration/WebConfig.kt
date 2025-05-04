package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.infrastructure.adapter.filter.CacheReloadAuthFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Web 필터 설정
 */
@Configuration
class WebConfig {

    /**
     * 캐시 재적재 API 인증 필터 등록
     */
    @Bean
    fun cacheReloadAuthFilterRegistration(cacheReloadAuthFilter: CacheReloadAuthFilter): FilterRegistrationBean<CacheReloadAuthFilter> {
        val registration = FilterRegistrationBean<CacheReloadAuthFilter>()
        registration.filter = cacheReloadAuthFilter
        registration.order = 1
        return registration
    }
}
