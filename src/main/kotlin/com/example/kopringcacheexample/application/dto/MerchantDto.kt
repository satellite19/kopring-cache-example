package com.example.kopringcacheexample.application.dto

import com.example.kopringcacheexample.domain.model.Merchant

/**
 * 가맹점 조회 요청 DTO
 */
data class MerchantRequest(
    val merchantId: String,
    val storeId: String
)

/**
 * 가맹점 응답 DTO
 */
data class MerchantResponse(
    val id: Long,
    val merchantId: String,
    val storeId: String,
    val merchantName: String,
    val merchantStatus: String
) {
    companion object {
        // Merchant 엔티티를 MerchantResponse로 변환
        fun fromEntity(merchant: Merchant): MerchantResponse {
            return MerchantResponse(
                id = merchant.id,
                merchantId = merchant.merchantId,
                storeId = merchant.storeId,
                merchantName = merchant.merchantName,
                merchantStatus = merchant.merchantStatus
            )
        }
    }
}
