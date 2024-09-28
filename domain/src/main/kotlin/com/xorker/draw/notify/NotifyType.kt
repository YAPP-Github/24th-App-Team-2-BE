package com.xorker.draw.notify

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.room.RoomId

sealed class NotifyType {
    class DiscordRandomMatchingNotifyType(
        name: String,
        language: LanguageType,
    ) : NotifyType() {
        val message: String = language.content + " ${name}님이 빠른 게임 상대를 기다리고 있어요."

        companion object {
            private fun from(name: String, language: String):
                DiscordRandomMatchingNotifyType = DiscordRandomMatchingNotifyType(name, getLanguageType(language))

            operator fun invoke(name: String, language: String): DiscordRandomMatchingNotifyType = from(name, language)
        }
    }

    class DiscordStartGameNotifyType(
        roomId: RoomId,
        language: LanguageType,
    ) : NotifyType() {
        val message: String = language.content + " ${roomId.value} 방에서 게임이 시작되었어요."

        companion object {
            private fun from(roomId: RoomId, language: String):
                DiscordStartGameNotifyType = DiscordStartGameNotifyType(roomId, getLanguageType(language))

            operator fun invoke(roomId: RoomId, language: String): DiscordStartGameNotifyType = from(roomId, language)
        }
    }

    companion object {
        fun getLanguageType(language: String): LanguageType {
            return when (language) {
                "ko" -> LanguageType.KOREAN
                "en" -> LanguageType.ENGLISH
                else -> throw UnSupportedException
            }
        }
    }
}

enum class LanguageType(
    val content: String,
) {
    KOREAN("\uD83C\uDDF0\uD83C\uDDF7"),
    ENGLISH("\uD83C\uDDFA\uD83C\uDDF8"),
    ;
}
