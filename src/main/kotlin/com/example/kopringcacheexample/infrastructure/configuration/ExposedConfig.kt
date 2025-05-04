package com.example.kopringcacheexample.infrastructure.configuration

import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.CardErrorCodeTable
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.MerchantTable
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.TodoTable
import com.example.kopringcacheexample.infrastructure.adapter.persistence.table.VirtualAccountErrorCodeTable
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import javax.sql.DataSource

/**
 * Exposed 설정
 */
@Configuration
class ExposedConfig(private val dataSource: DataSource) {

    @Bean
    fun database(): Database {
        return Database.connect(dataSource)
    }

    /**
     * Exposed 트랜잭션 매니저 설정
     */
    @Bean
    @DependsOn("database")
    fun transactionManager() = SpringTransactionManager(dataSource)

    /**
     * 데이터베이스 초기화 및 테이블 생성
     */
    @Bean
    @DependsOn("database")
    fun databaseInitializer(): Boolean {
        transaction {
            addLogger(StdOutSqlLogger)
            
            SchemaUtils.create(
                TodoTable,
                MerchantTable,
                CardErrorCodeTable,
                VirtualAccountErrorCodeTable
            )
        }
        
        return true
    }
}
