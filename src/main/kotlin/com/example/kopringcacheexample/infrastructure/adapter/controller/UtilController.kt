package com.example.kopringcacheexample.infrastructure.adapter.controller

import com.example.kopringcacheexample.application.service.EncryptionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 유틸리티 컨트롤러 - 테스트용
 */
@RestController
@RequestMapping("/util")
class UtilController(
    private val encryptionService: EncryptionService
) {

    /**
     * 카드번호 암호화 유틸리티
     */
    @GetMapping("/encrypt")
    fun encryptCardNumber(
        @RequestParam cardNumber: String,
        @RequestParam(defaultValue = "ABCDEF1234567890ABCDEF1234567890") secretKey: String
    ): Map<String, String> {
        val encryptedCardNumber = encryptionService.encrypt(cardNumber, secretKey)
        
        return mapOf(
            "originalCardNumber" to cardNumber,
            "encryptedCardNumber" to encryptedCardNumber
        )
    }
}
