package com.example.kopringcacheexample.infrastructure.adapter.filter

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

/**
 * HttpServletRequest 본문을 캐싱하는 래퍼 클래스
 * 여러 번 요청 본문을 읽을 수 있도록 지원
 */
open class RequestBodyCachingWrapper(
    request: HttpServletRequest
) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray

    init {
        // 요청 본문을 읽어서 캐싱
        cachedBody = request.inputStream.readBytes()
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(cachedBody)
        
        return object : ServletInputStream() {
            override fun read(): Int = byteArrayInputStream.read()
            
            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0
            
            override fun isReady(): Boolean = true
            
            override fun setReadListener(listener: ReadListener?) {
                throw UnsupportedOperationException("ReadListener is not supported")
            }
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(getInputStream()))
    }
}
