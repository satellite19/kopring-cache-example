package com.example.kopringcacheexample.infrastructure.adapter.filter

import jakarta.servlet.ServletOutputStream
import jakarta.servlet.WriteListener
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

/**
 * HttpServletResponse 본문을 캐싱하는 래퍼 클래스
 */
class ResponseBodyCachingWrapper(
    response: HttpServletResponse
) : HttpServletResponseWrapper(response) {

    private val outputStream = ByteArrayOutputStream()
    private var writer: PrintWriter? = null

    /**
     * 캐싱된 응답 본문을 문자열로 반환
     */
    val contentAsString: String
        get() = String(outputStream.toByteArray(), StandardCharsets.UTF_8)

    override fun getOutputStream(): ServletOutputStream {
        return object : ServletOutputStream() {
            override fun write(b: Int) {
                outputStream.write(b)
            }

            override fun isReady(): Boolean = true

            override fun setWriteListener(listener: WriteListener?) {
                throw UnsupportedOperationException("WriteListener is not supported")
            }
        }
    }

    override fun getWriter(): PrintWriter {
        if (writer == null) {
            writer = PrintWriter(OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true)
        }
        return writer!!
    }
}
