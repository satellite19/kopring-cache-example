package com.example.kopringcacheexample.infrastructure.adapter.controller

import com.example.kopringcacheexample.application.dto.MerchantRequest
import com.example.kopringcacheexample.application.dto.MerchantResponse
import com.example.kopringcacheexample.application.service.CacheManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 가맹점 정보 API 컨트롤러
 */
@RestController
@RequestMapping("/api/merchants")
class MerchantController(private val cacheManagementService: CacheManagementService) {

    /**
     * 가맹점 정보 조회
     */
    @GetMapping
    fun getMerchant(@RequestBody request: MerchantRequest): ResponseEntity<MerchantResponse> {
        val merchant = cacheManagementService.getMerchant(request.merchantId, request.storeId)
        return ResponseEntity.ok(merchant)
    }
}
