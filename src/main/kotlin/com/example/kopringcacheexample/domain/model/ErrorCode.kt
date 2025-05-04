package com.example.kopringcacheexample.domain.model

/**
 * 오류 코드를 위한 공통 인터페이스
 */
interface ErrorCode {
    val code: String
    val message: String
}
