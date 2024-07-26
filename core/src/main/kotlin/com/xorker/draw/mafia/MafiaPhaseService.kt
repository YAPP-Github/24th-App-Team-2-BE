package com.xorker.draw.mafia

import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.room.RoomId
import org.springframework.stereotype.Service

@Service
internal class MafiaPhaseService(
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
    private val startGameService: MafiaStartGameService,
    private val mafiaGameRepository: MafiaGameRepository,
) : MafiaPhaseUseCase {

    override fun startGame(roomId: RoomId): MafiaPhase.Ready {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            assertIs<MafiaPhase.Wait>(gameInfo.phase)
            startGameService.startMafiaGame(gameInfo)
        }

        mafiaGameMessenger.broadcastGameInfo(gameInfo)
        mafiaGameMessenger.broadcastGameReady(gameInfo)
        mafiaGameMessenger.broadcastPlayerTurnList(gameInfo)

        return phase
    }

    override fun playGame(roomId: RoomId): MafiaPhase.Playing {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val readyPhase = gameInfo.phase
            assertIs<MafiaPhase.Ready>(readyPhase)
            val playingPhase = readyPhase.toPlaying()
            gameInfo.phase = playingPhase
            playingPhase
        }

        mafiaPhaseMessenger.broadcastPlaying(gameInfo)

        return phase
    }

    private fun getGameInfo(roomId: RoomId): MafiaGameInfo {
        return mafiaGameRepository.getGameInfo(roomId) ?: throw NotFoundRoomException
    }
}
