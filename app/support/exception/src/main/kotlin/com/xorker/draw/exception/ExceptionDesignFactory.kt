package com.xorker.draw.exception

import com.xorker.draw.i18n.MessageService
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class ExceptionDesignFactory(
    private val messageService: MessageService,
) {
    fun generate(ex: XorkerException): ExceptionDesign {
        return when (ex) {
            is NotFoundRoomException,
            is MaxRoomException,
            -> generateToast(ex)

            else -> generateDialog(ex)
        }
    }

    private fun generateToast(ex: XorkerException): ToastResponse {
        val locale = LocaleContextHolder.getLocale()
        val text = messageService.getMessageOrNull("exception.${ex.code}.text", locale)
            ?: messageService.getMessage("exception.default.description", locale)

        return ToastResponse(text)
    }

    private fun generateDialog(ex: XorkerException): DialogResponse {
        val locale = LocaleContextHolder.getLocale()

        val title = messageService.getMessageOrNull("exception.${ex.code}.title", locale) ?: "ERROR" // TODO 기본 에러 처리 하기
        val description =
            messageService.getMessageOrNull("exception.${ex.code}.description", locale)
                ?: messageService.getMessage("exception.default.description", locale)
        val buttons = ex.getButtons().map { it.toResponse(messageService, locale) }

        return DialogResponse(title, description, buttons)
    }
}
