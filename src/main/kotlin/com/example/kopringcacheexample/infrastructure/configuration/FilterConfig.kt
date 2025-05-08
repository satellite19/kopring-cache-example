package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.infrastructure.adapter.filter.*
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.util.ContentCachingRequestWrapper

/**
 * 필터 설정 클래스
 */
@Configuration
class FilterConfig(
    private val txIdFilter: TxIdFilter,
    private val loggingFilter: LoggingFilter,
    private val adminAuthFilter: AdminAuthFilter,
    private val encryptionFilter: EncryptionFilter
) {

    /**
     * VirtualFilterChain을 구현한 필터 등록
     */
    @Bean
    fun virtualFilterChain(): FilterRegistrationBean<Filter> {
        val registrationBean = FilterRegistrationBean<Filter>()
        
        registrationBean.filter = object : Filter {
            override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
                val req = if (request is ContentCachingRequestWrapper) {
                    request
                } else {
                    ContentCachingRequestWrapper(request as jakarta.servlet.http.HttpServletRequest)
                }
                
                // 필터 체인 구성
                val filters = listOf(
                    txIdFilter,             // 1. TxId 필터
                    loggingFilter,          // 2. 로깅 필터
                    adminAuthFilter,        // 3. 관리자 인증 필터
                    encryptionFilter        // 4. 암호화 필터
                )
                
                // VirtualFilterChain 실행
                val virtualChain = VirtualFilterChain(chain, filters)
                virtualChain.doFilter(req, response)
            }
        }
        
        // 모든 요청에 필터 적용
        registrationBean.addUrlPatterns("/*")
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE
        
        return registrationBean
    }
}
