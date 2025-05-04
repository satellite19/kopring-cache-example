package com.example.kopringcacheexample.infrastructure.adapter.persistence.entity

import com.example.kopringcacheexample.domain.model.VirtualAccountErrorCode
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.VirtualAccountErrorCodeTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * 가상계좌 오류 코드 엔티티 (Exposed DAO)
 */
class VirtualAccountErrorCodeEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, VirtualAccountErrorCodeEntity>(VirtualAccountErrorCodeTable)
    
    var message by VirtualAccountErrorCodeTable.message
    
    /**
     * DAO 엔티티를 도메인 모델로 변환
     */
    fun toDomain(): VirtualAccountErrorCode {
        return VirtualAccountErrorCode(
            code = id.value,
            message = message
        )
    }
}
