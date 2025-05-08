package com.example.kopringcacheexample.domain.model

/**
 * 암호화 키 도메인 모델
 */
data class EncryptionKey(
    val id: Long,
    val clientId: String,
    val paymentType: String,
    val secretKey: String
)
