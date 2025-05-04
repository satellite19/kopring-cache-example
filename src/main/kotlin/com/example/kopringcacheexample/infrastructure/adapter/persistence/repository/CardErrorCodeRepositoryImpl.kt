package com.example.kopringcacheexample.infrastructure.adapter.persistence.repository

import com.example.kopringcacheexample.domain.model.CardErrorCode
import com.example.kopringcacheexample.domain.repository.CardErrorCodeRepository
import com.example.kopringcacheexample.infrastructure.adapter.persistence.entity.CardErrorCodeEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

/**
 * 카드 오류 코드 레포지토리 구현 (Exposed 사용)
 */
@Repository
class CardErrorCodeRepositoryImpl : CardErrorCodeRepository {

    /**
     * 모든 카드 오류 코드 조회
     */
    override fun findAll(): List<CardErrorCode> = transaction {
        CardErrorCodeEntity.all().map { it.toDomain() }
    }
    
    /**
     * 코드로 카드 오류 코드 조회
     */
    override fun findById(code: String): CardErrorCode? = transaction {
        CardErrorCodeEntity.findById(code)?.toDomain()
    }
    
    /**
     * 모든 카드 오류 코드 조회 (별칭)
     */
    override fun findAllCardErrorCodes(): List<CardErrorCode> = findAll()
    
    /**
     * 카드 오류 코드 저장
     */
    override fun save(cardErrorCode: CardErrorCode): CardErrorCode = transaction {
        val existingEntity = CardErrorCodeEntity.findById(cardErrorCode.code)
        
        if (existingEntity == null) {
            // 새로운 오류 코드 생성
            CardErrorCodeEntity.new(cardErrorCode.code) {
                message = cardErrorCode.message
            }.toDomain()
        } else {
            // 기존 오류 코드 업데이트
            existingEntity.message = cardErrorCode.message
            existingEntity.toDomain()
        }
    }
    
    /**
     * 여러 카드 오류 코드 저장
     */
    override fun saveAll(cardErrorCodes: List<CardErrorCode>): List<CardErrorCode> = transaction {
        cardErrorCodes.map { save(it) }
    }
    
    /**
     * 개수 조회
     */
    override fun count(): Long = transaction {
        CardErrorCodeEntity.count()
    }
}
