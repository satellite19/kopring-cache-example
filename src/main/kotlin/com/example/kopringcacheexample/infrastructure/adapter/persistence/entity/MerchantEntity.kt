package com.example.kopringcacheexample.infrastructure.adapter.persistence.entity

import com.example.kopringcacheexample.domain.model.Merchant
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.MerchantTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 가맹점 엔티티 (Exposed DAO)
 */
class MerchantEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MerchantEntity>(MerchantTable)
    
    var merchantId by MerchantTable.merchantId
    var storeId by MerchantTable.storeId
    var merchantName by MerchantTable.merchantName
    var merchantStatus by MerchantTable.merchantStatus
    
    /**
     * DAO 엔티티를 도메인 모델로 변환
     */
    fun toDomain(): Merchant {
        return Merchant(
            id = id.value,
            merchantId = merchantId,
            storeId = storeId,
            merchantName = merchantName,
            merchantStatus = merchantStatus
        )
    }
}
