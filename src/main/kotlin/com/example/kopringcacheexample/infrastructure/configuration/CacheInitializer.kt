package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.application.service.CacheManagementService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * 애플리케이션 시작 시 캐시 초기화
 */
@Component
class CacheInitializer(
    private val cacheManagementService: CacheManagementService
) : CommandLineRunner { //fixme

    /**
     * 애플리케이션 시작 시 모든 캐시 데이터 로드
     */
    override fun run(vararg args: String?) {
        try {
            cacheManagementService.reloadAllCaches()
        } catch (e: Exception) {
            // 시작 시점에 캐시 로딩 실패해도 애플리케이션은 계속 실행
            println("Failed to initialize cache on startup: ${e.message}")
        }
    }
}
