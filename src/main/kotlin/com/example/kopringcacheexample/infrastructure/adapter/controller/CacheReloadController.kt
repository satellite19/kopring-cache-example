package com.example.kopringcacheexample.infrastructure.adapter.controller

import com.example.kopringcacheexample.application.service.CacheManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 캐시 재적재 API 컨트롤러
 * - Basic Auth와 IP 체크가 적용됨
 */
@RestController
@RequestMapping("/api/cache-reload")
class CacheReloadController(private val cacheManagementService: CacheManagementService) {

    /**
     * 모든 캐시 재적재
     */
    @PostMapping("/all")
    fun reloadAllCaches(): ResponseEntity<Map<String, String>> {
        cacheManagementService.reloadAllCaches()
        return ResponseEntity.ok(mapOf("result" to "success", "message" to "All caches have been reloaded"))
    }

    /**
     * 특정 캐시 재적재
     */
    @PostMapping("/{cacheName}")
    fun reloadSpecificCache(@PathVariable cacheName: String): ResponseEntity<Map<String, String>> {
        cacheManagementService.reloadSpecificCache(cacheName)
        return ResponseEntity.ok(mapOf("result" to "success", "message" to "Cache '$cacheName' has been reloaded"))
    }
}
