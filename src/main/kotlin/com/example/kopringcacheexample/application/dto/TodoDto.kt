package com.example.kopringcacheexample.application.dto

import com.example.kopringcacheexample.domain.model.Todo
import java.time.LocalDateTime

/**
 * Todo 생성 요청 DTO
 */
data class CreateTodoRequest(
    val title: String,
    val description: String? = null
)

/**
 * Todo 수정 요청 DTO
 */
data class UpdateTodoRequest(
    val title: String?,
    val description: String?,
    val isDone: Boolean?
)

/**
 * Todo 응답 DTO
 */
data class TodoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        // Todo 엔티티를 TodoResponse로 변환
        fun fromEntity(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id,
                title = todo.title,
                description = todo.description,
                isDone = todo.isDone,
                createdAt = todo.createdAt,
                updatedAt = todo.updatedAt
            )
        }
    }
}
