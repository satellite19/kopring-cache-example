package com.example.kopringcacheexample.infrastructure.configuration

/**
 * 애플리케이션에서 사용하는 모든 캐시 이름을 관리하는 Enum
 */


object CacheConst {
    const val TODO_NAME = "todo"
    const val MERCHANT_NAME = "merchant"
    const val CARD_ERROR_CODE_NAME = "card_error_code"
    const val VIRTUAL_ACCOUNT_ERROR_CODE_NAME = "virtual_account_error_code"

}
enum class CacheNames(val cacheName: String) {
    TODO(CacheConst.TODO_NAME),
    MERCHANT(CacheConst.MERCHANT_NAME),
    CARD_ERROR_CODE(CacheConst.CARD_ERROR_CODE_NAME),
    VIRTUAL_ACCOUNT_ERROR_CODE(CacheConst.VIRTUAL_ACCOUNT_ERROR_CODE_NAME);

    companion object {
        // 캐시 이름으로 Enum 찾기
        fun fromCacheName(cacheName: String): CacheNames? {
            return values().find { it.cacheName == cacheName }
        }
        
        // 모든 캐시 이름 목록 반환
        fun getAllCacheNames(): List<String> {
            return values().map { it.cacheName }
        }
    }
}
