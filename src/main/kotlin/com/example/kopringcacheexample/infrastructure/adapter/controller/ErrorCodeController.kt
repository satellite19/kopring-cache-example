package com.example.kopringcacheexample.infrastructure.adapter.controller

import com.example.kopringcacheexample.application.dto.ErrorCodeResponse
import com.example.kopringcacheexample.application.service.CacheManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 오류 코드 API 컨트롤러
 */
@RestController
@RequestMapping("/api/error-codes")
class ErrorCodeController(private val cacheManagementService: CacheManagementService) {

    /**
     * 카드 오류 코드 조회
     */
    @GetMapping("/card/{errorCode}")
    fun getCardErrorCode(@PathVariable errorCode: String): ResponseEntity<ErrorCodeResponse> {
        val response = cacheManagementService.getCardErrorCode(errorCode)
        return ResponseEntity.ok(response)
    }

    /**
     * 가상계좌 오류 코드 조회
     */
    @GetMapping("/virtual-account/{errorCode}")
    fun getVirtualAccountErrorCode(@PathVariable errorCode: String): ResponseEntity<ErrorCodeResponse> {
        val response = cacheManagementService.getVirtualAccountErrorCode(errorCode)
        response
        return ResponseEntity.ok(response)
    }
}
