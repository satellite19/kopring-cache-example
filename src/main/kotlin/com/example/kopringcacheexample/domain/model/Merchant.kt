package com.example.kopringcacheexample.domain.model

import java.io.Serializable

/**
 * 가맹점 정보 도메인 모델
 */
data class Merchant(
    val id: Long = 0,
    val merchantId: String,
    val storeId: String,
    val merchantName: String,
    val merchantStatus: String
) : Serializable {
    // 복합키 생성 메서드
    fun getCacheKey(): String = "${merchantId}_${storeId}"
}
