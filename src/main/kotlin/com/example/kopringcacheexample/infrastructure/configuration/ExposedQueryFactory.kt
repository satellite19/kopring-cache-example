package com.example.kopringcacheexample.infrastructure.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@Component
class ExposedQueryFactory {
    private val logger = KotlinLogging.logger{}
    fun fetch(query: () -> Query): ResultRow? {
        val result = measureTimedValue {
            transaction {
                query().firstOrNull()
            }
        }
        logger.info{"fetch 쿼리 소요시간: ${result.duration.inWholeMilliseconds} ms"}
        return result.value
    }

    fun fetchAll(query: () -> Query): Iterable<ResultRow> {
        var sqlStatement = ""

        val result = measureTimedValue {
            transaction {
                val queryObj = query()
                sqlStatement = queryObj.prepareSQL(this)
                queryObj.toList()
            }
        }

        logger.info { "fetchAll 쿼리 소요시간: ${result.duration.inWholeMilliseconds} ms, SQL: $sqlStatement" }
        return result.value
    }


}