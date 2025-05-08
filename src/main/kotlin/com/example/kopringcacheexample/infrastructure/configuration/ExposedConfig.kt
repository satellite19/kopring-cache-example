package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.*
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

/**
 * Exposed 설정
 */
@Configuration
@EnableTransactionManagement
class ExposedConfig(
    @Autowired private val env: Environment
) {
    private val logger = LoggerFactory.getLogger(ExposedConfig::class.java)

    /**
     * DataSource 설정
     */
    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName") ?: "org.h2.Driver")
        dataSource.url = env.getProperty("spring.datasource.url") ?: "jdbc:h2:mem:testdb"
        dataSource.username = env.getProperty("spring.datasource.username") ?: "sa"
        dataSource.password = env.getProperty("spring.datasource.password") ?: ""
        return dataSource
    }

    /**
     * Exposed Database 설정
     */
    @Bean
    @Primary
    fun database(dataSource: DataSource): Database {
        return Database.connect(
            datasource = dataSource,
            databaseConfig = DatabaseConfig { }
        )
    }

    /**
     * Exposed 트랜잭션 매니저 설정
     */
    @Bean
    @DependsOn("database")
    fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)
}

/**
 * 테이블 초기화 컴포넌트
 * ExposedConfig와 분리하여 실행 순서를 명확히 함
 */
@Component
class DatabaseInitializer(
    private val database: Database
) {
    private val logger = LoggerFactory.getLogger(DatabaseInitializer::class.java)

    @PostConstruct
    fun initTables() {
        try {
            transaction(database) {
                // 모든 테이블 생성
                logger.info("Creating database tables...")
                SchemaUtils.drop(
                    EncryptionKeys,
                    MerchantTable,
                    TodoTable,
                    CardErrorCodeTable,
                    VirtualAccountErrorCodeTable
                )
                
                SchemaUtils.create(
                    EncryptionKeys,
                    MerchantTable,
                    TodoTable,
                    CardErrorCodeTable,
                    VirtualAccountErrorCodeTable
                )
                
                // EncryptionKeys 데이터 초기화
                logger.info("Initializing encryption keys data...")
                
                // 기본 암호화 키 삽입
                EncryptionKeys.insert { row ->
                    row[clientId] = "A0001"
                    row[paymentType] = "card"
                    row[secretKey] = "CARD_SECRET_KEY_A0001"
                }
                
                EncryptionKeys.insert { row ->
                    row[clientId] = "A0001"
                    row[paymentType] = "bank"
                    row[secretKey] = "BANK_SECRET_KEY_A0001"
                }
                
                EncryptionKeys.insert { row ->
                    row[clientId] = "A0002"
                    row[paymentType] = "card"
                    row[secretKey] = "CARD_SECRET_KEY_A0002"
                }
                
                EncryptionKeys.insert { row ->
                    row[clientId] = "A0002"
                    row[paymentType] = "bank"
                    row[secretKey] = "BANK_SECRET_KEY_A0002"
                }
            }
        } catch (e: Exception) {
            logger.error("Error initializing database tables", e)
        }
    }
}