package com.xorker.draw.mafia

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
internal class MafiaKeywordAdapter(
    private val wordJpaRepository: WordJpaRepository,
) : MafiaKeywordRepository {

    override fun getRandomKeyword(): MafiaKeyword? {
        val locale = LocaleContextHolder.getLocale()

        return wordJpaRepository.findRandomWord()?.toDomain(locale)
    }
}
