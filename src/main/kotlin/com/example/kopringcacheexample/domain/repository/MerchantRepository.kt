package com.example.kopringcacheexample.domain.repository

import com.example.kopringcacheexample.domain.model.Merchant

/**
 * 가맹점 레포지토리 인터페이스
 */
interface MerchantRepository {
    fun findAll(): List<Merchant>
    fun findByMerchantIdAndStoreId(merchantId: String, storeId: String): Merchant?
    fun findAllMerchants(): List<Merchant>
    fun save(merchant: Merchant): Merchant
    fun saveAll(merchants: List<Merchant>): List<Merchant>
    fun count(): Long
}
