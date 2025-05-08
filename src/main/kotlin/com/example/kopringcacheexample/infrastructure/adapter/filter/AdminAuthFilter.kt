package com.example.kopringcacheexample.infrastructure.adapter.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * 관리자 API 인증 필터
 */
@Configuration
class AdminAuthFilter(
    @Value("\${admin.auth-username:admin}") private val authUsername: String,
    @Value("\${admin.auth-password:password123}") private val authPassword: String
) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        // 관리자 API 경로가 아니면 통과
        if (!httpRequest.requestURI.startsWith("/admin")) {
            chain.doFilter(request, response)
            return
        }

        // Basic Auth 검증
        if (!isValidBasicAuth(httpRequest)) {
            sendUnauthorizedResponse(httpResponse, "인증 실패: 관리자 권한이 필요합니다.")
            return
        }

        chain.doFilter(request, response)
    }

    /**
     * Basic Auth 인증 검증
     */
    private fun isValidBasicAuth(request: HttpServletRequest): Boolean {
        val authHeader = request.getHeader("Authorization") ?: return false

        if (!authHeader.startsWith("Basic ")) {
            return false
        }

        try {
            val base64Credentials = authHeader.substring("Basic ".length).trim()
            val credentials = String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8)
            val values = credentials.split(":", limit = 2)

            return values.size == 2 && values[0] == authUsername && values[1] == authPassword
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 인증 실패 응답
     */
    private fun sendUnauthorizedResponse(response: HttpServletResponse, message: String) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader("WWW-Authenticate", "Basic realm=\"Admin Area\"")
        response.writer.write("{\"error\": \"$message\"}")
    }
}
