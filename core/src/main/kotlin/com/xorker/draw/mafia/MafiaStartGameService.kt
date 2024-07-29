package com.xorker.draw.mafia

import com.xorker.draw.timer.TimerRepository
import java.util.*
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
internal class MafiaStartGameService(
    private val mafiaKeywordRepository: MafiaKeywordRepository,
    private val timerRepository: TimerRepository,
) {

    internal fun startMafiaGame(gameInfo: MafiaGameInfo, nextStep: () -> Unit): MafiaPhase.Ready {
        val room = gameInfo.room
        val players = room.players
        val gameOption = gameInfo.gameOption

        val turnList = generateTurnList(players)

        val mafiaIndex = Random.nextInt(0, players.size)
        val keyword = mafiaKeywordRepository.getRandomKeyword(Locale.KOREAN) // TODO extract room locale

        val job = timerRepository.startTimer(gameOption.readyTime, nextStep)

        val phase = MafiaPhase.Ready(
            job = job,
            turnList = turnList,
            mafiaPlayer = players[mafiaIndex],
            keyword = keyword,
        )
        gameInfo.phase = phase

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
