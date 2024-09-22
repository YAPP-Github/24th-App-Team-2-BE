package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameRepository
import com.xorker.draw.mafia.MafiaKeywordRepository
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
internal class MafiaPhaseStartGameProcessor(
    private val mafiaKeywordRepository: MafiaKeywordRepository,
    private val timerRepository: TimerRepository,
    private val mafiaGameRepository: MafiaGameRepository,
) {
    private val random: Random = Random(System.currentTimeMillis())

    internal fun startMafiaGame(gameInfo: MafiaGameInfo, nextStep: () -> Unit): MafiaPhase.Ready {
        val room = gameInfo.room
        val players = room.players
        val gameOption = gameInfo.gameOption

        val turnList = players.shuffled(random)

        val mafiaIndex = random.nextInt(0, players.size)
        val keyword = mafiaKeywordRepository.getRandomKeyword(room.locale)

        timerRepository.startTimer(room.id, gameOption.readyTime, nextStep)

        val phase = MafiaPhase.Ready(
            jobKey = room.id,
            turnList = turnList,
            mafiaPlayer = players[mafiaIndex],
            keyword = keyword,
        )
        gameInfo.phase = phase

        mafiaGameRepository.saveGameInfo(gameInfo)

        return phase
    }
}
