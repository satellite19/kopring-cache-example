package com.example.kopringcacheexample.domain.repository

import com.example.kopringcacheexample.domain.model.Todo

/**
 * Todo 레포지토리 인터페이스
 */
interface TodoRepository {
    fun findAll(): List<Todo>
    fun findById(id: Long): Todo?
    fun save(todo: Todo): Todo
    fun deleteById(id: Long)
    fun existsById(id: Long): Boolean
}
