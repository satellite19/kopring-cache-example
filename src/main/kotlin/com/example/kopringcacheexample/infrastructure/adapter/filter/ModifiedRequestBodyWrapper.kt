package com.example.kopringcacheexample.infrastructure.adapter.filter

import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * 수정된 요청 본문으로 HttpServletRequest를 래핑하는 클래스
 */
class ModifiedRequestBodyWrapper(
    request: HttpServletRequest,
    private val modifiedBody: String
) : RequestBodyCachingWrapper(request) {

    /**
     * 수정된 본문으로 ServletInputStream 반환
     */
    override fun getInputStream(): ServletInputStream {
        val bodyBytes = modifiedBody.toByteArray(StandardCharsets.UTF_8)
        val byteArrayInputStream = ByteArrayInputStream(bodyBytes)
        
        return object : ServletInputStream() {
            override fun read(): Int = byteArrayInputStream.read()
            
            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0
            
            override fun isReady(): Boolean = true
            
            override fun setReadListener(listener: jakarta.servlet.ReadListener?) {
                throw UnsupportedOperationException("ReadListener is not supported")
            }
        }
    }

    /**
     * 수정된 본문으로 BufferedReader 반환
     */
    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(getInputStream(), StandardCharsets.UTF_8))
    }
}
