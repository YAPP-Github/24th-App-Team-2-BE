package com.xorker.draw.mafia

import org.springframework.stereotype.Component

@Component
internal class MafiaKeywordAdapter(
    private val wordJpaRepository: WordJpaRepository,
) : MafiaKeywordRepository {

    override fun getRandomKeyword(language: String): MafiaKeyword {
        val word = wordJpaRepository.findRandomWord(language)

        return word.toDomain()
    }
}
