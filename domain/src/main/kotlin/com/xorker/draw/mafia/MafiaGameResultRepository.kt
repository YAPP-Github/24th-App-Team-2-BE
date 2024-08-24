package com.xorker.draw.mafia

interface MafiaGameResultRepository {
    fun saveMafiaGameResult(gameInfo: MafiaGameInfo)
}
