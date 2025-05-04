package com.example.kopringcacheexample.application.dto

import com.example.kopringcacheexample.domain.model.ErrorCode

/**
 * 오류 코드 응답 DTO
 */
data class ErrorCodeResponse(
    val code: String,
    val message: String
) {
    companion object {
        // ErrorCode 인터페이스를 구현한 모든 엔티티를 ErrorCodeResponse로 변환
        fun fromEntity(errorCode: ErrorCode): ErrorCodeResponse {
            return ErrorCodeResponse(
                code = errorCode.code,
                message = errorCode.message
            )
        }
    }
}
