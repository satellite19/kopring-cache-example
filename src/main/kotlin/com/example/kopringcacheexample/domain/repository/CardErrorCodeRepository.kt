package com.example.kopringcacheexample.domain.repository

import com.example.kopringcacheexample.domain.model.CardErrorCode

/**
 * 카드 오류 코드 레포지토리 인터페이스
 */
interface CardErrorCodeRepository {
    fun findAll(): List<CardErrorCode>
    fun findById(code: String): CardErrorCode?
    fun findAllCardErrorCodes(): List<CardErrorCode>
    fun save(cardErrorCode: CardErrorCode): CardErrorCode
    fun saveAll(cardErrorCodes: List<CardErrorCode>): List<CardErrorCode>
    fun count(): Long
}
