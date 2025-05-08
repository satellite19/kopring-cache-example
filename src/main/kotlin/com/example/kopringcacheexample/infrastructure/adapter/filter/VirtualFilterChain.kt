package com.example.kopringcacheexample.infrastructure.adapter.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse

/**
 * 가상 필터 체인 구현
 * 여러 필터를 순차적으로 실행할 수 있는 체인
 */
class VirtualFilterChain(
    private val originalChain: FilterChain,
    private val filters: List<Filter>
) : FilterChain {

    private var currentPosition = 0

    override fun doFilter(request: ServletRequest, response: ServletResponse) {
        if (currentPosition < filters.size) {
            // 현재 위치의 필터 실행 및 현재 위치 증가
            val currentFilter = filters[currentPosition++]
            currentFilter.doFilter(request, response, this)
        } else {
            // 모든 필터 실행 완료 후 원래의 필터 체인 실행
            originalChain.doFilter(request, response)
        }
    }
}
