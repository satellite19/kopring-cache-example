package com.example.kopringcacheexample.domain.model

import java.io.Serializable
import java.time.LocalDateTime

/**
 * Todo 도메인 모델
 */
data class Todo(
    val id: Long = 0,
    var title: String,
    var description: String? = null,
    var isDone: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) : Serializable
