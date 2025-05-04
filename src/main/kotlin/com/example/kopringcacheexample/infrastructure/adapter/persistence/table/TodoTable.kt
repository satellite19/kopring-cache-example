package com.example.kopringcacheexample.infrastructure.adapter.persistence.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

/**
 * Todo 테이블 정의
 */
object TodoTable : LongIdTable("todos") {
    val title = varchar("title", 255)
    val description = varchar("description", 1000).nullable()
    val isDone = bool("is_done").default(false)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}
