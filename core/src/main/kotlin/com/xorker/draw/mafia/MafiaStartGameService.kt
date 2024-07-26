package com.xorker.draw.mafia

import com.xorker.draw.mafia.event.MafiaReadyExpiredEvent
import com.xorker.draw.timer.TimerRepository
import java.util.Locale
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
internal class MafiaStartGameService(
    private val mafiaKeywordRepository: MafiaKeywordRepository,
    private val timerRepository: TimerRepository,
) {

    fun startMafiaGame(gameInfo: MafiaGameInfo): MafiaPhase.Ready {
        val room = gameInfo.room
        val players = room.players

        val turnList = generateTurnList(players)

        room.clear()
        room.addAll(turnList)

        val mafiaIndex = Random.nextInt(0, players.size)
        val keyword = mafiaKeywordRepository.getRandomKeyword(Locale.KOREAN) // TODO extract room locale

        val phase = MafiaPhase.Ready(
            turnList = turnList,
            mafiaPlayer = players[mafiaIndex],
            keyword = keyword,
        )
        gameInfo.phase = phase

        timerRepository.startTimer(gameInfo.gameOption.readyShowingTime, MafiaReadyExpiredEvent(gameInfo))

        return phase
    }

    private fun generateTurnList(players: List<MafiaPlayer>): MutableList<MafiaPlayer> {
        val turnList = mutableListOf<MafiaPlayer>()
        players.forEach {
            turnList.add(it)
        }
        return turnList
    }
}
