package com.example.kopringcacheexample.infrastructure.adapter.persistence.entity

import com.example.kopringcacheexample.domain.model.Todo
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.TodoTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

/**
 * Todo 엔티티 (Exposed DAO)
 */
class TodoEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TodoEntity>(TodoTable)
    
    var title by TodoTable.title
    var description by TodoTable.description
    var isDone by TodoTable.isDone
    var createdAt by TodoTable.createdAt
    var updatedAt by TodoTable.updatedAt
    
    /**
     * DAO 엔티티를 도메인 모델로 변환
     */
    fun toDomain(): Todo {
        return Todo(
            id = id.value,
            title = title,
            description = description,
            isDone = isDone,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    /**
     * 도메인 모델로부터 DAO 엔티티 생성 (ID 제외)
     */
    fun fromDomain(todo: Todo) {
        title = todo.title
        description = todo.description
        isDone = todo.isDone
        updatedAt = LocalDateTime.now()
    }
}
