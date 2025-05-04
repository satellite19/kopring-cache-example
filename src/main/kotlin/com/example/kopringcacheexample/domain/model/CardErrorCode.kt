package com.example.kopringcacheexample.domain.model

import java.io.Serializable

/**
 * 카드 오류 코드 도메인 모델
 */
data class CardErrorCode(
    override val code: String,
    override val message: String
) : ErrorCode, Serializable
