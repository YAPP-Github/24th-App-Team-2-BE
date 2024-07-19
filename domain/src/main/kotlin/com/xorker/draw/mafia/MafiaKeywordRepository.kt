package com.xorker.draw.mafia

import java.util.Locale

interface MafiaKeywordRepository {
    fun getRandomKeyword(locale: Locale): MafiaKeyword
}
