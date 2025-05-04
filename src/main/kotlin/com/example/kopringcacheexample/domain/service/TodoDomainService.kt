package com.example.kopringcacheexample.domain.service

import com.example.kopringcacheexample.domain.model.Todo
import com.example.kopringcacheexample.domain.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Todo 도메인 서비스
 */
@Service
class TodoDomainService(private val todoRepository: TodoRepository) {

    /**
     * 모든 할 일 목록 조회
     */
    fun findAll(): List<Todo> {
        return todoRepository.findAll()
    }

    /**
     * ID로 할 일 조회
     */
    fun findById(id: Long): Todo {
        return todoRepository.findById(id) 
            ?: throw NoSuchElementException("Todo not found with id: $id")
    }

    /**
     * 할 일 생성
     */
    @Transactional
    fun create(title: String, description: String?): Todo {
        val todo = Todo(
            title = title,
            description = description,
            isDone = false
        )
        return todoRepository.save(todo)
    }

    /**
     * 할 일 수정
     */
    @Transactional
    fun update(id: Long, title: String?, description: String?, isDone: Boolean?): Todo {
        val todo = findById(id)
        
        title?.let { todo.title = it }
        description?.let { todo.description = it }
        isDone?.let { todo.isDone = it }
        todo.updatedAt = LocalDateTime.now()
        
        return todoRepository.save(todo)
    }

    /**
     * 할 일 삭제
     */
    @Transactional
    fun delete(id: Long) {
        // 존재 여부 확인
        if (!todoRepository.existsById(id)) {
            throw NoSuchElementException("Todo not found with id: $id")
        }
        todoRepository.deleteById(id)
    }
}
