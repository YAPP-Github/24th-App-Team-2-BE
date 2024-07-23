package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaGamePlayingPhaseStatusException
import com.xorker.draw.mafia.event.MafiaReadyExpiredEvent
import com.xorker.draw.room.RoomId
import com.xorker.draw.timer.TimerRepository
import java.util.Locale
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
internal class MafiaStartGameService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaKeywordRepository: MafiaKeywordRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val timerRepository: TimerRepository,
) : MafiaStartGameUseCase {

    override fun startMafiaGame(roomId: RoomId) {
        val gameInfo = mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidMafiaGamePlayingPhaseStatusException

        val room = gameInfo.room
        val players = room.players

        val turnList = generateTurnList(players)

        room.clear()
        room.addAll(turnList)

        val mafiaIndex = Random.nextInt(0, players.size)
        val keyword = mafiaKeywordRepository.getRandomKeyword(Locale.KOREAN) // TODO extract room locale

        gameInfo.phase = MafiaPhase.Playing(
            turnList = turnList,
            mafiaPlayer = players[mafiaIndex],
            keyword = keyword,
        )

        timerRepository.startTimer(gameInfo.gameOption.readyShowingTime, MafiaReadyExpiredEvent(gameInfo))

        mafiaGameMessenger.broadcastGameInfo(gameInfo)

        mafiaGameMessenger.broadcastGameReady(gameInfo)

        mafiaGameMessenger.broadcastPlayerTurnList(gameInfo)
    }

    private fun generateTurnList(players: List<MafiaPlayer>): MutableList<MafiaPlayer> {
        val turnList = mutableListOf<MafiaPlayer>()
        players.forEach {
            turnList.add(it)
        }
        return turnList
    }
}
