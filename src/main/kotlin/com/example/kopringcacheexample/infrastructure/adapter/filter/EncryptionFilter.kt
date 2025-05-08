package com.example.kopringcacheexample.infrastructure.adapter.filter

import com.example.kopringcacheexample.application.service.EncryptionService
import com.example.kopringcacheexample.domain.repository.EncryptionKeyRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.ContentCachingRequestWrapper
import java.nio.charset.StandardCharsets

/**
 * 암호화된 카드 번호 복호화 필터
 */
@Configuration
class EncryptionFilter(
    private val encryptionKeyRepository: EncryptionKeyRepository,
    private val encryptionService: EncryptionService,
    private val objectMapper: ObjectMapper
) : Filter {

    private val logger = LoggerFactory.getLogger(EncryptionFilter::class.java)

    companion object {
        private const val ENCRYPT_CARD_NO_KEY = "encryptCardNo"
        private const val CLIENT_ID_KEY = "clientId"
        private const val PAYMENT_TYPE_KEY = "paymentType"
        private const val CARD_NO_KEY = "cardNo"
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        
        // JSON 요청인 경우에만 처리
        if (isJsonRequest(httpRequest)) {
            val wrapper = if (httpRequest is ContentCachingRequestWrapper) {
                httpRequest
            } else {
                ContentCachingRequestWrapper(httpRequest)
            }
            
            // 원래 필터 체인 실행
            chain.doFilter(wrapper, response)
            
            // 암호화된 카드 번호가 있는지 확인하고 복호화
            try {
                decryptCardNoIfPresent(wrapper)
            } catch (e: Exception) {
                logger.error("Error during card number decryption", e)
            }
        } else {
            chain.doFilter(request, response)
        }
    }

    /**
     * 암호화된 카드 번호가 있는 경우 복호화
     */
    private fun decryptCardNoIfPresent(request: ContentCachingRequestWrapper) {
        val contentAsByteArray = request.contentAsByteArray
        if (contentAsByteArray.isEmpty()) {
            return
        }

        val body = String(contentAsByteArray, StandardCharsets.UTF_8)
        if (body.isBlank()) {
            return
        }

        val jsonNode = objectMapper.readTree(body)
        
        // 암호화된 카드 번호가 있는지 확인
        val encryptCardNo = jsonNode.get(ENCRYPT_CARD_NO_KEY)?.asText()
        if (encryptCardNo.isNullOrBlank()) {
            return
        }
        
        // 클라이언트 ID와 결제 유형 확인
        val clientId = jsonNode.get(CLIENT_ID_KEY)?.asText()
        val paymentType = jsonNode.get(PAYMENT_TYPE_KEY)?.asText()
        
        if (clientId.isNullOrBlank() || paymentType.isNullOrBlank()) {
            logger.warn("Client ID or payment type is missing for encrypted card")
            return
        }
        
        // 암호화 키 조회
        val encryptionKey = encryptionKeyRepository.findByClientIdAndPaymentType(clientId, paymentType)
        if (encryptionKey == null) {
            logger.warn("Encryption key not found for clientId: $clientId, paymentType: $paymentType")
            return
        }
        
        // 카드 번호 복호화
        val decryptedCardNo = encryptionService.decrypt(encryptCardNo, encryptionKey.secretKey)
        
        // 여기서는 이미 요청이 처리되었으므로 복호화된 값을 로그로 출력만 한다
        // 실제 서버에서는 MDC에 보관하거나 필요시 다른 방법으로 처리해야 함
        logger.info("Decrypted card number for request: ${request.requestURI}, cardNo: $decryptedCardNo")
    }


    /**
     * 요청이 JSON인지 확인
     */
    private fun isJsonRequest(request: HttpServletRequest): Boolean {
        val contentType = request.contentType ?: return false
        return contentType.contains("application/json", ignoreCase = true)
    }
}
