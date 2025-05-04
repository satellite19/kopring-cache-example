package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.domain.model.CardErrorCode
import com.example.kopringcacheexample.domain.model.Merchant
import com.example.kopringcacheexample.domain.model.VirtualAccountErrorCode
import com.example.kopringcacheexample.domain.repository.CardErrorCodeRepository
import com.example.kopringcacheexample.domain.repository.MerchantRepository
import com.example.kopringcacheexample.domain.repository.VirtualAccountErrorCodeRepository
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

/**
 * 테스트를 위한 초기 데이터 삽입
 */
@Component
@DependsOn("databaseInitializer")
class DataInitializer(
    private val merchantRepository: MerchantRepository,
    private val cardErrorCodeRepository: CardErrorCodeRepository,
    private val virtualAccountErrorCodeRepository: VirtualAccountErrorCodeRepository
) {

    @PostConstruct
    fun init() {
        transaction {
            // 가맹점 테스트 데이터
            if (merchantRepository.count() == 0L) {
                val merchants = listOf(
                    Merchant(merchantId = "M001", storeId = "S001", merchantName = "테스트 가맹점1", merchantStatus = "ACTIVE"),
                    Merchant(merchantId = "M001", storeId = "S002", merchantName = "테스트 가맹점2", merchantStatus = "ACTIVE"),
                    Merchant(merchantId = "M002", storeId = "S001", merchantName = "테스트 가맹점3", merchantStatus = "INACTIVE")
                )
                merchantRepository.saveAll(merchants)
            }

            // 카드 오류 코드 테스트 데이터
            if (cardErrorCodeRepository.count() == 0L) {
                val cardErrorCodes = listOf(
                    CardErrorCode(code = "CE001", message = "카드 인증 실패"),
                    CardErrorCode(code = "CE002", message = "잔액 부족"),
                    CardErrorCode(code = "CE003", message = "카드 만료"),
                    CardErrorCode(code = "CE004", message = "분실 카드"),
                    CardErrorCode(code = "CE005", message = "한도 초과")
                )
                cardErrorCodeRepository.saveAll(cardErrorCodes)
            }

            // 가상계좌 오류 코드 테스트 데이터
            if (virtualAccountErrorCodeRepository.count() == 0L) {
                val virtualAccountErrorCodes = listOf(
                    VirtualAccountErrorCode(code = "VA001", message = "계좌번호 오류"),
                    VirtualAccountErrorCode(code = "VA002", message = "은행 점검 중"),
                    VirtualAccountErrorCode(code = "VA003", message = "입금 한도 초과"),
                    VirtualAccountErrorCode(code = "VA004", message = "일일 한도 초과"),
                    VirtualAccountErrorCode(code = "VA005", message = "시스템 오류")
                )
                virtualAccountErrorCodeRepository.saveAll(virtualAccountErrorCodes)
            }
        }
    }
}
