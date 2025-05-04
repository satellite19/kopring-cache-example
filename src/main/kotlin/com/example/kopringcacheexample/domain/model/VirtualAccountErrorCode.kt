package com.example.kopringcacheexample.domain.model

import java.io.Serializable

/**
 * 가상계좌 오류 코드 도메인 모델
 */
data class VirtualAccountErrorCode(
    override val code: String,
    override val message: String
) : ErrorCode, Serializable
