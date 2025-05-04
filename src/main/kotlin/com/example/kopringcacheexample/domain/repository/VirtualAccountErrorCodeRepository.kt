package com.example.kopringcacheexample.domain.repository

import com.example.kopringcacheexample.domain.model.VirtualAccountErrorCode

/**
 * 가상계좌 오류 코드 레포지토리 인터페이스
 */
interface VirtualAccountErrorCodeRepository {
    fun findAll(): List<VirtualAccountErrorCode>
    fun findById(code: String): VirtualAccountErrorCode?
    fun findAllVirtualAccountErrorCodes(): List<VirtualAccountErrorCode>
    fun save(virtualAccountErrorCode: VirtualAccountErrorCode): VirtualAccountErrorCode
    fun saveAll(virtualAccountErrorCodes: List<VirtualAccountErrorCode>): List<VirtualAccountErrorCode>
    fun count(): Long
}
