package com.example.kopringcacheexample.domain.repository

import com.example.kopringcacheexample.domain.model.EncryptionKey

/**
 * 암호화 키 저장소 인터페이스
 */
interface EncryptionKeyRepository {
    
    /**
     * 클라이언트 ID와 결제 유형으로 암호화 키 조회
     */
    fun findByClientIdAndPaymentType(clientId: String, paymentType: String): EncryptionKey?
}
