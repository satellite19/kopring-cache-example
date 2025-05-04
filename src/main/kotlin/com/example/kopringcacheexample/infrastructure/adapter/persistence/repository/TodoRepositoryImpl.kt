package com.example.kopringcacheexample.infrastructure.adapter.persistence.repository

import com.example.kopringcacheexample.domain.model.Todo
import com.example.kopringcacheexample.domain.repository.TodoRepository
import com.example.kopringcacheexample.infrastructure.adapter.persistence.entity.TodoEntity
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.TodoTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Todo 레포지토리 구현 (Exposed 사용)
 */
@Repository
class TodoRepositoryImpl : TodoRepository {

    /**
     * 모든 할 일 조회
     */
    override fun findAll(): List<Todo> = transaction {
        TodoEntity.all()
            .orderBy(TodoTable.id to SortOrder.ASC)
            .map { it.toDomain() }
    }

    /**
     * ID로 할 일 조회
     */
    override fun findById(id: Long): Todo? = transaction {
        TodoEntity.findById(id)?.toDomain()
    }

    /**
     * 할 일 저장
     */
    override fun save(todo: Todo): Todo = transaction {
        if (todo.id == 0L) {
            // 새로운 Todo 생성
            TodoEntity.new {
                title = todo.title
                description = todo.description
                isDone = todo.isDone
                createdAt = todo.createdAt
                updatedAt = LocalDateTime.now()
            }.toDomain()
        } else {
            // 기존 Todo 업데이트
            val entity = TodoEntity.findById(todo.id)
                ?: throw NoSuchElementException("Todo not found with id: ${todo.id}")
                
            entity.fromDomain(todo)
            entity.toDomain()
        }
    }

    /**
     * ID로 할 일 삭제
     */
    override fun deleteById(id: Long): Unit = transaction {
        TodoEntity.findById(id)?.delete()
    }
    
    /**
     * 존재 여부 확인
     */
    override fun existsById(id: Long): Boolean = transaction {
        TodoEntity.findById(id) != null
    }
}
