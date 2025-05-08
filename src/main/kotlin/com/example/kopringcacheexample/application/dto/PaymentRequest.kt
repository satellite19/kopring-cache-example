package com.example.kopringcacheexample.application.dto

/**
 * 결제 요청 DTO
 */
data class PaymentRequest(
    val txId: String? = null,
    val clientId: String,
    val paymentType: String,
    val encryptCardNo: String? = null,
    val cardNo: String? = null,
    val amount: Long,
    val currency: String = "KRW"
)
