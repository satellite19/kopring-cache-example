package com.example.kopringcacheexample.domain.service

import com.example.kopringcacheexample.domain.model.CardErrorCode
import com.example.kopringcacheexample.domain.model.VirtualAccountErrorCode
import com.example.kopringcacheexample.domain.repository.CardErrorCodeRepository
import com.example.kopringcacheexample.domain.repository.VirtualAccountErrorCodeRepository
import org.springframework.stereotype.Service

/**
 * 오류 코드 도메인 서비스
 */
@Service
class ErrorCodeDomainService(
    private val cardErrorCodeRepository: CardErrorCodeRepository,
    private val virtualAccountErrorCodeRepository: VirtualAccountErrorCodeRepository
) {

    /**
     * 모든 카드 오류 코드 조회
     */
    fun findAllCardErrorCodes(): List<CardErrorCode> {
        return cardErrorCodeRepository.findAll()
    }

    /**
     * 카드 오류 코드 단건 조회
     */
    fun findCardErrorCode(errorCode: String): CardErrorCode {
        return cardErrorCodeRepository.findById(errorCode)
            ?: throw NoSuchElementException("Card error code not found: $errorCode")
    }

    /**
     * 모든 가상계좌 오류 코드 조회
     */
    fun findAllVirtualAccountErrorCodes(): List<VirtualAccountErrorCode> {
        return virtualAccountErrorCodeRepository.findAll()
    }

    /**
     * 가상계좌 오류 코드 단건 조회
     */
    fun findVirtualAccountErrorCode(errorCode: String): VirtualAccountErrorCode {
        return virtualAccountErrorCodeRepository.findById(errorCode)
            ?: throw NoSuchElementException("Virtual account error code not found: $errorCode")
    }
}
