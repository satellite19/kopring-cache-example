package com.example.kopringcacheexample.infrastructure.adapter.persistence.entity

import com.example.kopringcacheexample.domain.model.CardErrorCode
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.CardErrorCodeTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 카드 오류 코드 엔티티 (Exposed DAO)
 */
class CardErrorCodeEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CardErrorCodeEntity>(CardErrorCodeTable)
    
    var message by CardErrorCodeTable.message
    
    /**
     * DAO 엔티티를 도메인 모델로 변환
     */
    fun toDomain(): CardErrorCode {
        return CardErrorCode(
            code = id.value,
            message = message
        )
    }
}

/*
class CardErrorCodeEntity(id: EntityID<String>, CardErrorCodeTable) : Entity<String>(id) {
    companion object : EntityClass<String, CardErrorCodeEntity>()

    var message by CardErrorCodeTable.message

    */
/**
     * DAO 엔티티를 도메인 모델로 변환
     *//*

    fun toDomain(): CardErrorCode {
        return CardErrorCode(
            code = id.value,
            message = message
        )
    }
}*/
