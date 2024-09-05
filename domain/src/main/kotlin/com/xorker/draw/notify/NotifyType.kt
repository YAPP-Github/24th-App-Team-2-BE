package com.xorker.draw.notify

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.room.RoomId

sealed class NotifyType {
    class DiscordRandomMatchingNotifyType(
        name: String,
        language: String,
    ) : NotifyType() {
        val message: String

        private var _language: LanguageType? = null

        init {
            _language = getLanguageType(language)
            message = _language!!.content + " ${name}님이 빠른 게임 상대를 기다리고 있어요."
        }
    }

    class DiscordStartGameNotifyType(
        roomId: RoomId,
        language: String,
    ) : NotifyType() {
        val message: String

        private var _language: LanguageType? = null

        init {
            _language = getLanguageType(language)
            message = _language!!.content + " ${roomId.value} 방에서 게임이 시작되었어요."
        }
    }

    fun getLanguageType(language: String): LanguageType {
        if (language == "ko") {
            return LanguageType.KOREAN
        } else if (language == "en") {
            return LanguageType.ENGLISH
        }
        throw UnSupportedException
    }
}

enum class LanguageType(
    val content: String,
) {
    KOREAN("\uD83C\uDDF0\uD83C\uDDF7"),
    ENGLISH("\uD83C\uDDFA\uD83C\uDDF8"),
    ;
}
