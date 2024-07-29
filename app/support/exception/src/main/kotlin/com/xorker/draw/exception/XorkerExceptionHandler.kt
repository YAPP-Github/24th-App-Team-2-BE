package com.xorker.draw.exception

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class XorkerExceptionHandler(
    private val messageSource: MessageSource,
) {

    fun convert(ex: XorkerException): ExceptionResponse {
        val locale = LocaleContextHolder.getLocale()

        val title = messageSource.getMessage("exception.${ex.code}.title", null, "ERROR", locale) // TODO 기본 에러 처리 하기
        val description = messageSource.getMessage("exception.${ex.code}.description", null, null, locale)
            ?: messageSource.getMessage("exception.default.description", null, locale)
        val buttons = ex.getButtons().map { it.toResponse(messageSource, locale) }
        return ExceptionResponse(ex.code, title, description, buttons)
    }
}
