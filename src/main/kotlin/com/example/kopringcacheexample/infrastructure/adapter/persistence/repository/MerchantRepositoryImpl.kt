package com.example.kopringcacheexample.infrastructure.adapter.persistence.repository

import com.example.kopringcacheexample.domain.model.Merchant
import com.example.kopringcacheexample.domain.repository.MerchantRepository
import com.example.kopringcacheexample.infrastructure.adapter.persistence.entity.MerchantEntity
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.MerchantTable
import com.example.kopringcacheexample.infrastructure.configuration.ExposedQueryFactory
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

/**
 * 가맹점 레포지토리 구현 (Exposed 사용)
 */
@Repository
class MerchantRepositoryImpl(
    private val exposedQueryFactory: ExposedQueryFactory
) : MerchantRepository {

    /**
     * 모든 가맹점 조회
     */
    override fun findAll(): List<Merchant> {
        return exposedQueryFactory.fetchAll {
            MerchantTable.selectAll()
        }.map {
            Merchant(
                merchantId = it[MerchantTable.merchantId],
                storeId = it[MerchantTable.storeId],
                merchantName = it[MerchantTable.merchantName],
                merchantStatus = it[MerchantTable.merchantStatus],
            )
        }
    }


    /**
     * 가맹점 ID와 상점 ID로 가맹점 조회
     */
    override fun findByMerchantIdAndStoreId(merchantId: String, storeId: String): Merchant? = transaction {
        MerchantEntity.find {
            (MerchantTable.merchantId eq merchantId) and (MerchantTable.storeId eq storeId)
        }.firstOrNull()?.toDomain()
    }
    
    /**
     * 모든 가맹점 조회 (별칭)
     */
    override fun findAllMerchants(): List<Merchant> = findAll()
    
    /**
     * 가맹점 저장
     */
    override fun save(merchant: Merchant): Merchant = transaction {
        if (merchant.id == 0L) {
            // 새로운 가맹점 생성
            MerchantEntity.new {
                merchantId = merchant.merchantId
                storeId = merchant.storeId
                merchantName = merchant.merchantName
                merchantStatus = merchant.merchantStatus
            }.toDomain()
        } else {
            // 기존 가맹점 조회
            val entity = MerchantEntity.findById(merchant.id)
                ?: throw NoSuchElementException("Merchant not found with id: ${merchant.id}")
                
            // 값 업데이트
            entity.merchantId = merchant.merchantId
            entity.storeId = merchant.storeId
            entity.merchantName = merchant.merchantName
            entity.merchantStatus = merchant.merchantStatus
            
            entity.toDomain()
        }
    }
    
    /**
     * 여러 가맹점 저장
     */
    override fun saveAll(merchants: List<Merchant>): List<Merchant> = transaction {
        merchants.map { save(it) }
    }
    
    /**
     * 개수 조회
     */
    override fun count(): Long = transaction {
        MerchantEntity.count()
    }
}
