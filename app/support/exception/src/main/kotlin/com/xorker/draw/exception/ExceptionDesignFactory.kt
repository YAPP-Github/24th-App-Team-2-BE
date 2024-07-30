package com.xorker.draw.exception

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class ExceptionDesignFactory(
    private val messageSource: MessageSource,
) {
    fun generate(ex: XorkerException): ExceptionDesign {
        when (ex) {
            is NotFoundRoomException,
            is MaxRoomException,
            -> return generateToast(ex)

            else -> return generateDialog(ex)
        }
    }

    private fun generateToast(ex: XorkerException): ToastResponse {
        val locale = LocaleContextHolder.getLocale()
        val text = messageSource.getMessage("exception.${ex.code}.text", null, "ERROR", locale)
            ?: messageSource.getMessage("exception.default.description", null, locale)

        return ToastResponse(text)
    }

    private fun generateDialog(ex: XorkerException): DialogResponse {
        val locale = LocaleContextHolder.getLocale()

        val title = messageSource.getMessage("exception.${ex.code}.title", null, "ERROR", locale) // TODO 기본 에러 처리 하기
        val description = messageSource.getMessage("exception.${ex.code}.description", null, null, locale)
            ?: messageSource.getMessage("exception.default.description", null, locale)
        val buttons = ex.getButtons().map { it.toResponse(messageSource, locale) }

        return DialogResponse(title, description, buttons)
    }
}
