package com.example.kopringcacheexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableCaching // 캐시 기능 활성화
@EnableScheduling // 스케줄링 기능 활성화
class KopringCacheExampleApplication

fun main(args: Array<String>) {
    runApplication<KopringCacheExampleApplication>(*args)
}
