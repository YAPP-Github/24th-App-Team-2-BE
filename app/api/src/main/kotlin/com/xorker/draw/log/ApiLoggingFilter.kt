package com.xorker.draw.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.draw.support.logging.logger
import com.xorker.draw.support.logging.registerRequestId
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@WebFilter(urlPatterns = ["/api/*"])
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiLoggingFilter(
    private val objectMapper: ObjectMapper,
) : Filter {
    private val logger = logger()

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        registerRequestId()
        val request = ContentCachingRequestWrapper(servletRequest as HttpServletRequest)
        val response = ContentCachingResponseWrapper(servletResponse as HttpServletResponse)

        try {
            chain.doFilter(request, response)
        } finally {
            logger.info(generateLog(request, response))
            response.copyBodyToResponse()
        }
    }

    private fun generateLog(request: ContentCachingRequestWrapper, response: ContentCachingResponseWrapper): String {
        val data = mapOf(
            // Request 부분
            "method" to request.method,
            "uri" to request.requestURI,
            "query" to request.queryString,
            "header" to request.headerNames.asSequence().map { it to request.getHeader(it) }.toMap(),
            "requestBody" to request.contentAsString,

            // Response 부분
            "status" to response.status,
            "responseBody" to String(response.contentAsByteArray),

            "requestId" to MDC.get("requestId"),
            "userId" to MDC.get("userId"),
        )

        return objectMapper.writeValueAsString(data)
    }
}
