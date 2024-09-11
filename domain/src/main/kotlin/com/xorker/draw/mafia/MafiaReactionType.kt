package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException

enum class MafiaReactionType(
    private val description: String,
) {
    HAMSTER("HAMSTER"),
    BEAR("BEAR"),
    HEART("HEART"),
    POOP("POOP"),
    BEST("BEST"),
    QUESTION("QUESTION"),
    ;

    companion object {
        fun of(description: String): MafiaReactionType {
            try {
                return MafiaReactionType.valueOf(description)
            } catch (ex: IllegalArgumentException) {
                throw InvalidRequestValueException
            }
        }
    }
}
