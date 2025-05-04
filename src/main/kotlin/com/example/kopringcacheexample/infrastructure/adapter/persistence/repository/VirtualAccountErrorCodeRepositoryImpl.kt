package com.example.kopringcacheexample.infrastructure.adapter.persistence.repository

import com.example.kopringcacheexample.domain.model.VirtualAccountErrorCode
import com.example.kopringcacheexample.domain.repository.VirtualAccountErrorCodeRepository
import com.example.kopringcacheexample.infrastructure.adapter.persistence.entity.VirtualAccountErrorCodeEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

/**
 * 가상계좌 오류 코드 레포지토리 구현 (Exposed 사용)
 */
@Repository
class VirtualAccountErrorCodeRepositoryImpl : VirtualAccountErrorCodeRepository {

    /**
     * 모든 가상계좌 오류 코드 조회
     */
    override fun findAll(): List<VirtualAccountErrorCode> = transaction {
        VirtualAccountErrorCodeEntity.all().map { it.toDomain() }
    }
    
    /**
     * 코드로 가상계좌 오류 코드 조회
     */
    override fun findById(code: String): VirtualAccountErrorCode? = transaction {
        VirtualAccountErrorCodeEntity.findById(code)?.toDomain()
    }
    
    /**
     * 모든 가상계좌 오류 코드 조회 (별칭)
     */
    override fun findAllVirtualAccountErrorCodes(): List<VirtualAccountErrorCode> = findAll()
    
    /**
     * 가상계좌 오류 코드 저장
     */
    override fun save(virtualAccountErrorCode: VirtualAccountErrorCode): VirtualAccountErrorCode = transaction {
        val existingEntity = VirtualAccountErrorCodeEntity.findById(virtualAccountErrorCode.code)
        
        if (existingEntity == null) {
            // 새로운 오류 코드 생성
            VirtualAccountErrorCodeEntity.new(virtualAccountErrorCode.code) {
                message = virtualAccountErrorCode.message
            }.toDomain()
        } else {
            // 기존 오류 코드 업데이트
            existingEntity.message = virtualAccountErrorCode.message
            existingEntity.toDomain()
        }
    }
    
    /**
     * 여러 가상계좌 오류 코드 저장
     */
    override fun saveAll(virtualAccountErrorCodes: List<VirtualAccountErrorCode>): List<VirtualAccountErrorCode> = transaction {
        virtualAccountErrorCodes.map { save(it) }
    }
    
    /**
     * 개수 조회
     */
    override fun count(): Long = transaction {
        VirtualAccountErrorCodeEntity.count()
    }
}
