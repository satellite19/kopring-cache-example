package com.example.kopringcacheexample.infrastructure.adapter.filter

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.BufferedReader
import java.util.*

/**
 * 트랜잭션 ID 필터
 * Request Body에서 txId를 추출하여 MDC에 추가하는 필터
 */
@Configuration
class TxIdFilter(
    private val objectMapper: ObjectMapper
) : Filter {

    companion object {
        private const val TX_ID_KEY = "txId"
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest

        // JSON 요청인 경우에만 처리
        if (isJsonRequest(httpRequest)) {
            val wrapper = ContentCachingRequestWrapper(httpRequest)
            
            // 필터 체인 실행 후 Request Body 추출
            chain.doFilter(wrapper, response)
            
            // Request Body에서 txId 추출
            val txId = extractTxIdFromBody(wrapper)
            
            // MDC에 txId 설정
            MDC.put(TX_ID_KEY, txId ?: generateTxId())
            try {
                // 이미 필터 체인이 실행되었으므로 여기서는 아무것도 하지 않음
            } finally {
                MDC.remove(TX_ID_KEY)
            }
        } else {
            // JSON 요청이 아닌 경우 기본 txId 생성 후 설정
            MDC.put(TX_ID_KEY, generateTxId())
            try {
                chain.doFilter(request, response)
            } finally {
                MDC.remove(TX_ID_KEY)
            }
        }
    }

    /**
     * Request Body에서 txId 추출
     */
    private fun extractTxIdFromBody(request: ContentCachingRequestWrapper): String? {
        try {
            val contentAsByteArray = request.contentAsByteArray
            if (contentAsByteArray.isEmpty()) {
                return null
            }

            val body = String(contentAsByteArray, Charsets.UTF_8)
            if (body.isBlank()) {
                return null
            }

            val jsonNode = objectMapper.readTree(body)
            return jsonNode.get(TX_ID_KEY)?.asText()
        } catch (e: JsonParseException) {
            // JSON 파싱 실패시 null 반환
            return null
        } catch (e: Exception) {
            // 기타 예외 발생시 null 반환
            return null
        }
    }

    /**
     * 새로운 txId 생성
     */
    private fun generateTxId(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 요청이 JSON인지 확인
     */
    private fun isJsonRequest(request: HttpServletRequest): Boolean {
        val contentType = request.contentType ?: return false
        return contentType.contains("application/json", ignoreCase = true)
    }

}
