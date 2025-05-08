package com.example.kopringcacheexample.infrastructure.adapter.controller

import com.example.kopringcacheexample.application.dto.PaymentRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.bind.annotation.*

/**
 * 필터 테스트를 위한 컨트롤러
 */
@RestController
@RequestMapping("/api")
class TestController {

    private val logger: Logger = LoggerFactory.getLogger(TestController::class.java)

    /**
     * 일반 API 엔드포인트 - 모든 필터 테스트
     */
    @PostMapping("/payment")
    fun processPayment(@RequestBody request: PaymentRequest): Map<String, Any> {
        logger.info("Payment processing with request: $request")
        
        // txId 필터 테스트를 위한 MDC 값 확인
        val txId = MDC.get("txId") ?: "No txId found in MDC"
        
        val response = mutableMapOf<String, Any>(
            "success" to true,
            "message" to "Payment processed successfully",
            "txId" to txId
        )
        
        // 암호화 필터가 제대로 동작했는지 카드번호 확인
        if (request.cardNo != null) {
            val maskedCardNo = maskCardNumber(request.cardNo)
            response["maskedCardNo"] = maskedCardNo
        }
        
        return response
    }

    /**
     * 관리자 API 엔드포인트 - 관리자 인증 필터 테스트
     */
    @GetMapping("/admin/system/status")
    fun getSystemStatus(): Map<String, Any> {
        logger.info("Admin API called for system status")
        return mapOf(
            "status" to "Online",
            "uptime" to "10 days, 4 hours",
            "activeUsers" to 2458,
            "cpuUsage" to "24%",
            "memoryUsage" to "1.8GB / 4GB"
        )
    }

    /**
     * 캐시 재로드 API 엔드포인트 - 캐시 재로드 인증 필터 테스트
     */
    @PostMapping("/cache-reload")
    fun reloadCache(): Map<String, Any> {
        logger.info("Cache reload requested")
        return mapOf(
            "success" to true,
            "message" to "Cache has been successfully reloaded",
            "timestamp" to System.currentTimeMillis()
        )
    }

    /**
     * 카드번호 마스킹 처리
     */
    private fun maskCardNumber(cardNumber: String): String {
        if (cardNumber.length < 8) return cardNumber
        
        val visiblePrefix = cardNumber.substring(0, 4)
        val visibleSuffix = cardNumber.substring(cardNumber.length - 4)
        val maskedPart = "*".repeat(cardNumber.length - 8)
        
        return "$visiblePrefix$maskedPart$visibleSuffix"
    }
}
