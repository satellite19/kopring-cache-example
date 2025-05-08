package com.example.kopringcacheexample.infrastructure.adapter.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ping")
class PingController {

    @GetMapping
    fun ping(): Map<String, Any> {
        return mapOf(
            "status" to "UP",
            "message" to "Server is running",
            "timestamp" to System.currentTimeMillis()
        )
    }
}
