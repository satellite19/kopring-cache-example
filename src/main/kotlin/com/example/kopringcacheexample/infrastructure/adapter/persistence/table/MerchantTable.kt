package com.example.kopringcacheexample.infrastructure.adapter.persistence.table

import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * 가맹점 테이블 정의
 */
object MerchantTable : LongIdTable("merchants") {
    val merchantId = varchar("merchant_id", 50)
    val storeId = varchar("store_id", 50)
    val merchantName = varchar("merchant_name", 255)
    val merchantStatus = varchar("merchant_status", 50)
    
    // 복합 유니크 인덱스 추가
    init {
        uniqueIndex(merchantId, storeId)
    }
}
