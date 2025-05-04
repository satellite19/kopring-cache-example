package com.example.kopringcacheexample.domain.service

import com.example.kopringcacheexample.domain.model.Merchant
import com.example.kopringcacheexample.domain.repository.MerchantRepository
import org.springframework.stereotype.Service

/**
 * 가맹점 도메인 서비스
 */
@Service
class MerchantDomainService(private val merchantRepository: MerchantRepository) {

    /**
     * 모든 가맹점 목록 조회
     */
    fun findAll(): List<Merchant> {
        return merchantRepository.findAll()
    }

    /**
     * 가맹점 ID와 상점 ID로 가맹점 정보 조회
     */
    fun findByMerchantIdAndStoreId(merchantId: String, storeId: String): Merchant {
        return merchantRepository.findByMerchantIdAndStoreId(merchantId, storeId)
            ?: throw NoSuchElementException("Merchant not found with merchantId: $merchantId and storeId: $storeId")
    }
}
