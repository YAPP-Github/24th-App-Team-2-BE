package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaKeywordRepository
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.MafiaPlayer
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
internal class MafiaPhaseStartGameProcessor(
    private val mafiaKeywordRepository: MafiaKeywordRepository,
    private val timerRepository: TimerRepository,
) {

    internal fun startMafiaGame(gameInfo: MafiaGameInfo, nextStep: () -> Unit): MafiaPhase.Ready {
        val room = gameInfo.room
        val players = room.players
        val gameOption = gameInfo.gameOption

        val turnList = generateTurnList(players)

        val mafiaIndex = Random.nextInt(0, players.size)
        val keyword = mafiaKeywordRepository.getRandomKeyword(room.locale)

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
