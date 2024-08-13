package com.xorker.draw.mafia

interface MafiaKeywordRepository {
    fun getRandomKeyword(language: String): MafiaKeyword
}
