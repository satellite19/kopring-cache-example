package com.example.kopringcacheexample.application.service

import com.example.kopringcacheexample.application.dto.ErrorCodeResponse
import com.example.kopringcacheexample.application.dto.MerchantResponse
import com.example.kopringcacheexample.domain.service.ErrorCodeDomainService
import com.example.kopringcacheexample.domain.service.MerchantDomainService
import com.example.kopringcacheexample.infrastructure.configuration.CacheConst
import com.example.kopringcacheexample.infrastructure.configuration.CacheNames
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 캐시 관리를 위한 애플리케이션 서비스
 */
@Service
class CacheManagementService(
    private val merchantDomainService: MerchantDomainService,
    private val errorCodeDomainService: ErrorCodeDomainService,
    private val cacheManager: CacheManager
) {

    /**
     * 가맹점 정보 조회 (캐시 적용)
     */
    @Cacheable(value = [CacheConst.MERCHANT_NAME], key = "#merchantId + '_' + #storeId", unless = "#result == null")
    fun getMerchant(merchantId: String, storeId: String): MerchantResponse {
        val merchant = merchantDomainService.findByMerchantIdAndStoreId(merchantId, storeId)
        return MerchantResponse.fromEntity(merchant)
    }

    /**
     * 카드 오류 코드 조회 (캐시 적용)
     */
    @Cacheable(value = [CacheConst.CARD_ERROR_CODE_NAME], key = "#errorCode", unless = "#result == null")
    fun getCardErrorCode(errorCode: String): ErrorCodeResponse {
        val cardErrorCode = errorCodeDomainService.findCardErrorCode(errorCode)
        return ErrorCodeResponse.fromEntity(cardErrorCode)
    }

    /**
     * 가상계좌 오류 코드 조회 (캐시 적용)
     */
    @Cacheable(value = [CacheConst.VIRTUAL_ACCOUNT_ERROR_CODE_NAME], key = "#errorCode", unless = "#result == null")
    fun getVirtualAccountErrorCode(errorCode: String): ErrorCodeResponse {
        val virtualAccountErrorCode = errorCodeDomainService.findVirtualAccountErrorCode(errorCode)
        return ErrorCodeResponse.fromEntity(virtualAccountErrorCode)
    }

    /**
     * 모든 캐시 재적재
     */
    fun reloadAllCaches() {
        // 가맹점 캐시 재적재
        clearCache(CacheNames.MERCHANT.cacheName)
        merchantDomainService.findAll().forEach { merchant ->
            val response = MerchantResponse.fromEntity(merchant)
            putInCache(CacheNames.MERCHANT.cacheName, "${merchant.merchantId}_${merchant.storeId}", response)
        }

        // 카드 오류 코드 캐시 재적재
        clearCache(CacheNames.CARD_ERROR_CODE.cacheName)
        errorCodeDomainService.findAllCardErrorCodes().forEach { errorCode ->
            val response = ErrorCodeResponse.fromEntity(errorCode)
            putInCache(CacheNames.CARD_ERROR_CODE.cacheName, errorCode.code, response)
        }

        // 가상계좌 오류 코드 캐시 재적재
        clearCache(CacheNames.VIRTUAL_ACCOUNT_ERROR_CODE.cacheName)
        errorCodeDomainService.findAllVirtualAccountErrorCodes().forEach { errorCode ->
            val response = ErrorCodeResponse.fromEntity(errorCode)
            putInCache(CacheNames.VIRTUAL_ACCOUNT_ERROR_CODE.cacheName, errorCode.code, response)
        }
    }

    /**
     * 특정 캐시만 재적재
     */
    fun reloadSpecificCache(cacheName: String) {
        when (cacheName) {
            CacheNames.MERCHANT.cacheName -> {
                clearCache(cacheName)
                merchantDomainService.findAll().forEach { merchant ->
                    val response = MerchantResponse.fromEntity(merchant)
                    putInCache(cacheName, "${merchant.merchantId}_${merchant.storeId}", response)
                }
            }
            CacheNames.CARD_ERROR_CODE.cacheName -> {
                clearCache(cacheName)
                errorCodeDomainService.findAllCardErrorCodes().forEach { errorCode ->
                    val response = ErrorCodeResponse.fromEntity(errorCode)
                    putInCache(cacheName, errorCode.code, response)
                }
            }
            CacheNames.VIRTUAL_ACCOUNT_ERROR_CODE.cacheName -> {
                clearCache(cacheName)
                errorCodeDomainService.findAllVirtualAccountErrorCodes().forEach { errorCode ->
                    val response = ErrorCodeResponse.fromEntity(errorCode)
                    putInCache(cacheName, errorCode.code, response)
                }
            }
            else -> throw IllegalArgumentException("Invalid cache name: $cacheName")
        }
    }

    /**
     * 캐시 초기화 (내부용)
     */
    private fun clearCache(cacheName: String) {
        cacheManager.getCache(cacheName)?.clear()
    }
    
    /**
     * 캐시에 데이터 저장 (내부용)
     */
    private fun <T> putInCache(cacheName: String, key: String, value: T) {
        cacheManager.getCache(cacheName)?.put(key, value)
    }
}
