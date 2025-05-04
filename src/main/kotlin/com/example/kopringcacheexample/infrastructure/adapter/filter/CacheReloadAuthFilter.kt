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
 * 캐시 재적재 API를 위한 인증 필터
 */
@Configuration
class CacheReloadAuthFilter(
    @Value("\${cache.reload.allowed-ips}") private val allowedIpsString: String,
    @Value("\${cache.reload.auth-username}") private val authUsername: String,
    @Value("\${cache.reload.auth-password}") private val authPassword: String
) : Filter {

    private val allowedIps: Set<String> = allowedIpsString.split(",").map { it.trim() }.toSet()

    /**
     * 요청에 대한 인증과 IP 검증
     */
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        // 캐시 재적재 API 경로가 아니면 통과
        if (!httpRequest.requestURI.startsWith("/api/cache-reload")) {
            chain.doFilter(request, response)
            return
        }

        // IP 검증
        val clientIp = getClientIp(httpRequest)
        if (!isAllowedIp(clientIp)) {
            sendUnauthorizedResponse(httpResponse, "Forbidden IP: $clientIp")
            return
        }

        // Basic Auth 검증
        if (!isValidBasicAuth(httpRequest)) {
            sendUnauthorizedResponse(httpResponse, "Invalid authentication")
            return
        }

        chain.doFilter(request, response)
    }

    /**
     * 클라이언트 IP 추출
     */
    private fun getClientIp(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Forwarded-For")
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }
        if (ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true)) {
            ip = request.remoteAddr
        }
        return ip.split(",").firstOrNull()?.trim() ?: ""
    }

    /**
     * IP 허용 목록 검증
     */
    private fun isAllowedIp(clientIp: String): Boolean {
        return allowedIps.contains(clientIp)
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
        response.writer.write("{\"error\": \"$message\"}")
    }
}
