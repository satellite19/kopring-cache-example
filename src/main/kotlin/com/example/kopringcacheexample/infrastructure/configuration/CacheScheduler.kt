package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.application.service.CacheManagementService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * 4시간마다 캐시 자동 동기화
 */
@Component
class CacheScheduler(
    private val cacheManagementService: CacheManagementService
) {

    /**
     * 4시간마다 캐시 재적재
     */
    @Scheduled(fixedRate = 4 * 60 * 60 * 1000) // 4시간 (밀리초)
    fun reloadAllCachesScheduled() {
        try {
            cacheManagementService.reloadAllCaches()
        } catch (e: Exception) {
            println("Failed to reload caches on schedule: ${e.message}")
        }
    }
}
