package com.example.kopringcacheexample.infrastructure.adapter.persistence.table

import org.jetbrains.exposed.sql.Table

/**
 * 암호화 키 테이블 정의
 */
object EncryptionKeys : Table("encryption_keys") {
    val id = long("id").autoIncrement()
    val clientId = varchar("client_id", 50)
    val paymentType = varchar("payment_type", 20)
    val secretKey = varchar("secret_key", 128)
    
    override val primaryKey = PrimaryKey(id)
    
    init {
        index(isUnique = true, clientId, paymentType)
    }
}
