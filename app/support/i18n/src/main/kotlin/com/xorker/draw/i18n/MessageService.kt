package com.xorker.draw.i18n

import com.xorker.draw.exception.NotDefinedMessageCodeException
import com.xorker.draw.support.logging.logger
import java.util.Locale
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Component

@Component
class MessageService(
    private val messageSource: MessageSource,
) {
    private val logger = logger()

    fun getMessage(code: String, locale: Locale): String {
        try {
            return messageSource.getMessage(code, null, locale)
        } catch (e: NoSuchMessageException) {
            logger.error("i18n Error - $code", e)
            throw NotDefinedMessageCodeException(code, locale, e)
        }
    }

    fun getMessageOrNull(code: String, locale: Locale): String? {
        try {
            return messageSource.getMessage(code, null, locale)
        } catch (e: NoSuchMessageException) {
            logger.warn("i18n Error - $code", e)
            return null
        }
    }
}
