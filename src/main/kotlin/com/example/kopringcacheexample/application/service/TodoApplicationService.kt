package com.example.kopringcacheexample.application.service

import com.example.kopringcacheexample.application.dto.CreateTodoRequest
import com.example.kopringcacheexample.application.dto.TodoResponse
import com.example.kopringcacheexample.application.dto.UpdateTodoRequest
import com.example.kopringcacheexample.domain.service.TodoDomainService
import com.example.kopringcacheexample.infrastructure.configuration.CacheConst
import com.example.kopringcacheexample.infrastructure.configuration.CacheNames
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Todo 애플리케이션 서비스
 */
@Service
class TodoApplicationService(private val todoDomainService: TodoDomainService) {

    /**
     * 모든 할 일 조회
     */
    fun getAllTodos(): List<TodoResponse> {
        return todoDomainService.findAll()
            .map { TodoResponse.fromEntity(it) }
    }

    /**
     * 단일 할 일 조회 (캐시 적용)
     */
    @Cacheable(value = [CacheConst.TODO_NAME], key = "#id", unless = "#result == null")
    fun getTodoById(id: Long): TodoResponse {
        return TodoResponse.fromEntity(todoDomainService.findById(id))
    }

    /**
     * 할 일 생성
     */
    fun createTodo(request: CreateTodoRequest): TodoResponse {
        val todo = todoDomainService.create(request.title, request.description)
        return TodoResponse.fromEntity(todo)
    }

    /**
     * 할 일 수정
     */
    fun updateTodo(id: Long, request: UpdateTodoRequest): TodoResponse {
        val todo = todoDomainService.update(id, request.title, request.description, request.isDone)
        return TodoResponse.fromEntity(todo)
    }

    /**
     * 할 일 삭제
     */
    fun deleteTodo(id: Long) {
        todoDomainService.delete(id)
    }
}
