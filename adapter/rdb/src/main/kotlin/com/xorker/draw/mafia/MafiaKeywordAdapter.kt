package com.xorker.draw.mafia

import java.util.Locale
import org.springframework.stereotype.Component

@Component
internal class MafiaKeywordAdapter(
    private val wordJpaRepository: WordJpaRepository,
) : MafiaKeywordRepository {

    override fun getRandomKeyword(locale: Locale): MafiaKeyword {
        val word = wordJpaRepository.findRandomWord(locale.language)

        return word.toDomain()
    }
}
