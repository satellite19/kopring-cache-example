package com.example.kopringcacheexample.infrastructure.adapter.controller

import com.example.kopringcacheexample.application.dto.CreateTodoRequest
import com.example.kopringcacheexample.application.dto.TodoResponse
import com.example.kopringcacheexample.application.dto.UpdateTodoRequest
import com.example.kopringcacheexample.application.service.TodoApplicationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Todo REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/todos")
class TodoController(private val todoApplicationService: TodoApplicationService) {

    /**
     * 모든 할 일 조회
     */
    @GetMapping
    fun getAllTodos(): ResponseEntity<List<TodoResponse>> {
        val todos = todoApplicationService.getAllTodos()
        return ResponseEntity.ok(todos)
    }

    /**
     * 단일 할 일 조회
     */
    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Long): ResponseEntity<TodoResponse> {
        val todo = todoApplicationService.getTodoById(id)
        return ResponseEntity.ok(todo)
    }

    /**
     * 할 일 생성
     */
    @PostMapping
    fun createTodo(@RequestBody request: CreateTodoRequest): ResponseEntity<TodoResponse> {
        val createdTodo = todoApplicationService.createTodo(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo)
    }

    /**
     * 할 일 수정
     */
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @RequestBody request: UpdateTodoRequest
    ): ResponseEntity<TodoResponse> {
        val updatedTodo = todoApplicationService.updateTodo(id, request)
        return ResponseEntity.ok(updatedTodo)
    }

    /**
     * 할 일 삭제
     */
    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long): ResponseEntity<Void> {
        todoApplicationService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }
}
