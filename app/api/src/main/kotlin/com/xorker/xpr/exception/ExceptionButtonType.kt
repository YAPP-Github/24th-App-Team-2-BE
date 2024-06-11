package com.xorker.xpr.exception

import com.xorker.xpr.exception.ExceptionButtonAction.CloseDialog
import java.util.*
import org.springframework.context.MessageSource

enum class ExceptionButtonType(
    private val i18nCode: String,
    private val action: ExceptionButtonAction,
) {
    OK("exception.button.ok", CloseDialog),
    CANCEL("exception.button.cancel", CloseDialog),
    CLOSE("exception.button.close", CloseDialog),
    ;

    private val responseMap = HashMap<Locale, ExceptionButtonResponse>()

    fun toResponse(source: MessageSource, locale: Locale): ExceptionButtonResponse {
        if (responseMap.containsKey(locale).not()) {
            val response = ExceptionButtonResponse(
                text = source.getMessage(this.i18nCode, null, locale),
                action = this.action,
            )

            responseMap[locale] = response
        }

        return responseMap[locale]!!
    }
}

private val buttonOkCancel = listOf(ExceptionButtonType.OK, ExceptionButtonType.CANCEL)
private val buttonOkClose = listOf(ExceptionButtonType.OK, ExceptionButtonType.CLOSE)

fun XprException.getButtons(): List<ExceptionButtonType> {
    return when (this) {
        InvalidRequestValueException,
        UnAuthorizedException,
        -> buttonOkClose

        UnAuthenticationException,
        is UnknownException,
        -> buttonOkCancel
    }
}