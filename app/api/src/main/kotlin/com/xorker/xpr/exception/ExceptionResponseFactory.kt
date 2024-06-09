package com.xorker.xpr.exception

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExceptionResponseFactory(
    private val messageSource: MessageSource,
) {
    fun create(ex: XprException): ResponseEntity<ExceptionResponse> {
        return create(ex.getStatus(), ex)
    }

    fun create(status: HttpStatus, ex: XprException): ResponseEntity<ExceptionResponse> {
        val locale = LocaleContextHolder.getLocale()

        val title = messageSource.getMessage("exception.${ex.code}.title", null, locale)
        val description = messageSource.getMessage("exception.${ex.code}.description", null, locale)
            ?: messageSource.getMessage("exception.default.description", null, locale)

        // TODO Button
        val responseBody = ExceptionResponse(ex.code, title, description)

        return ResponseEntity
            .status(status)
            .body(responseBody)
    }

    private fun XprException.getStatus(): HttpStatus {
        return when (this) {
            is UnAuthenticationException -> HttpStatus.UNAUTHORIZED
            is UnAuthorizedException -> HttpStatus.FORBIDDEN

            // Base Exception
            is ClientException -> HttpStatus.BAD_REQUEST
            is ServerException, is CriticalException -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
