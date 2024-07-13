package com.xorker.draw.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExceptionResponseFactory(
    private val exceptionHandler: XorkerExceptionHandler,
) {
    fun create(ex: XorkerException): ResponseEntity<ExceptionResponse> {
        return create(ex.getStatus(), ex)
    }

    fun create(status: HttpStatus, ex: XorkerException): ResponseEntity<ExceptionResponse> {
        val responseBody = exceptionHandler.convert(ex)

        return ResponseEntity
            .status(status)
            .body(responseBody)
    }

    private fun XorkerException.getStatus(): HttpStatus {
        return when (this) {
            is UnAuthenticationException -> HttpStatus.UNAUTHORIZED
            is UnAuthorizedException -> HttpStatus.FORBIDDEN

            // Base Exception
            is ClientException -> HttpStatus.BAD_REQUEST
            is ServerException, is CriticalException -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
