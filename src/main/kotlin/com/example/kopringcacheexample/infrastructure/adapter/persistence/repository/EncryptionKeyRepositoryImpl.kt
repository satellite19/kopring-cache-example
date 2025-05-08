package com.example.kopringcacheexample.infrastructure.adapter.persistence.repository

import com.example.kopringcacheexample.domain.model.EncryptionKey
import com.example.kopringcacheexample.domain.repository.EncryptionKeyRepository
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.EncryptionKeys
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.andWhere
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

/**
 * 암호화 키 저장소 구현체
 */
@Repository
class EncryptionKeyRepositoryImpl(
    @Qualifier("database") private val database: Database
) : EncryptionKeyRepository {

    private val logger = LoggerFactory.getLogger(EncryptionKeyRepositoryImpl::class.java)

    /**
     * 클라이언트 ID와 결제 유형으로 암호화 키 조회
     * 캐시 적용
     */
    @Cacheable(value = ["encryptionKeys"], key = "#clientId + '-' + #paymentType")
    override fun findByClientIdAndPaymentType(clientId: String, paymentType: String): EncryptionKey? {
        logger.info("Finding encryption key for clientId: $clientId, paymentType: $paymentType")
        
        return transaction(database) {
            val query = EncryptionKeys.selectAll()
                .andWhere { EncryptionKeys.clientId eq clientId }
                .andWhere { EncryptionKeys.paymentType eq paymentType }
            
            query.singleOrNull()?.let {
                EncryptionKey(
                    id = it[EncryptionKeys.id],
                    clientId = it[EncryptionKeys.clientId],
                    paymentType = it[EncryptionKeys.paymentType],
                    secretKey = it[EncryptionKeys.secretKey]
                )
            }
        }
    }
}
