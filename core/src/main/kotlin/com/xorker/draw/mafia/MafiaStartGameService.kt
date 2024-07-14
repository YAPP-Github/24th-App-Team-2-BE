package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidGameStatusException
import com.xorker.draw.exception.NotFoundWordException
import com.xorker.draw.room.RoomId
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
internal class MafiaStartGameService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaKeywordRepository: MafiaKeywordRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaStartGameUseCase {

    override fun startMafiaGame(roomId: RoomId) {
        val gameInfo = mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidGameStatusException

        val room = gameInfo.room
        val players = room.players

        val turnList = mutableListOf<MafiaPlayer>()
        players.forEach {
            turnList.add(it)
        }

        val mafia = Random.nextInt(0, players.size)
        val keyword = mafiaKeywordRepository.getRandomKeyword() ?: throw NotFoundWordException

        gameInfo.phase = MafiaPhase.Playing(
            turnList = turnList,
            mafiaPlayer = players[mafia],
            keyword = keyword,
        )
        gameInfo.gameOption = MafiaGameOption()

        mafiaGameMessenger.broadcastGameInfo(gameInfo)
    }
}
