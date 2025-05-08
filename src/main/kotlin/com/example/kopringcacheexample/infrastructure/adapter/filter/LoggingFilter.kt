package com.example.kopringcacheexample.infrastructure.adapter.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * 요청/응답 로깅 필터
 */
@Configuration
class LoggingFilter : Filter {

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        
        // ContentCachingRequestWrapper와 ContentCachingResponseWrapper 사용
        val requestWrapper = if (httpRequest is ContentCachingRequestWrapper) {
            httpRequest
        } else {
            ContentCachingRequestWrapper(httpRequest)
        }
        
        val responseWrapper = ContentCachingResponseWrapper(httpResponse)
        
        // 필터 체인 실행 전 요청 기본 정보 로깅
        logRequestStart(requestWrapper)
        
        // 필터 체인 실행
        chain.doFilter(requestWrapper, responseWrapper)
        
        // 필터 체인 실행 후 요청/응답 로깅
        logRequestBody(requestWrapper)
        logResponse(responseWrapper)
        
        // 응답 본문 복사
        responseWrapper.copyBodyToResponse()
    }

    /**
     * Request 기본 정보 로깅
     */
    private fun logRequestStart(request: ContentCachingRequestWrapper) {
        val sb = StringBuilder()
        
        sb.append("\n===== REQUEST START =====\n")
        sb.append("URI: ${request.requestURI}\n")
        sb.append("Method: ${request.method}\n")
        sb.append("Content-Type: ${request.contentType}\n")
        
        // 헤더 로깅
        sb.append("Headers: {\n")
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            val headerValue = request.getHeader(headerName)
            sb.append("  $headerName: $headerValue\n")
        }
        sb.append("}\n")
        
        logger.info(sb.toString())
    }
    
    /**
     * Request 본문 로깅
     */
    private fun logRequestBody(request: ContentCachingRequestWrapper) {
        val sb = StringBuilder()
        
        sb.append("\n===== REQUEST BODY =====\n")
        
        // Request 본문 로깅
        val content = request.contentAsByteArray
        if (content.isNotEmpty()) {
            val contentString = String(content, StandardCharsets.UTF_8)
            sb.append("Body: $contentString\n")
        } else {
            sb.append("Body: <empty>\n")
        }
        
        logger.info(sb.toString())
    }

    /**
     * Response 로깅
     */
    private fun logResponse(response: ContentCachingResponseWrapper) {
        val sb = StringBuilder()
        
        sb.append("\n===== RESPONSE =====\n")
        sb.append("Status: ${response.status}\n")
        sb.append("Content-Type: ${response.contentType}\n")
        
        // 헤더 로깅
        sb.append("Headers: {\n")
        for (headerName in response.headerNames) {
            val headerValue = response.getHeader(headerName)
            sb.append("  $headerName: $headerValue\n")
        }
        sb.append("}\n")
        
        // Response 본문 로깅 
        val content = response.contentAsByteArray
        if (content.isNotEmpty()) {
            val contentString = String(content, StandardCharsets.UTF_8)
            sb.append("Body: $contentString\n")
        }
        
        logger.info(sb.toString())
    }
}
