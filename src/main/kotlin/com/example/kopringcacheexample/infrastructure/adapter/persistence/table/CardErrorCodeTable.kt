package com.example.kopringcacheexample.infrastructure.adapter.persistence.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * 카드 오류 코드 테이블 정의
 */
object CardErrorCodeTable : IdTable<String>("card_error_codes") {
    override val id = varchar("error_code", 20).entityId()
    val message = varchar("error_message", 255)

    override val primaryKey = PrimaryKey(id)


}
