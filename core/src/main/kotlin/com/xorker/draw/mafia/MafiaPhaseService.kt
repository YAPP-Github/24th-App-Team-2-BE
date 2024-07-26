package com.xorker.draw.mafia

import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.room.RoomId
import com.xorker.draw.timer.TimerRepository
import org.springframework.stereotype.Service

@Service
internal class MafiaPhaseService(
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
    private val startGameService: MafiaStartGameService,
    private val mafiaGameService: MafiaGameService,
    private val mafiaGameRepository: MafiaGameRepository,
    private val timerRepository: TimerRepository,
) : MafiaPhaseUseCase {

    override fun startGame(roomId: RoomId): MafiaPhase.Ready {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            assertIs<MafiaPhase.Wait>(gameInfo.phase)
            startGameService.startMafiaGame(gameInfo)
        }

        mafiaPhaseMessenger.broadcastPhase(gameInfo)

        return phase
    }

    override fun playGame(roomId: RoomId): MafiaPhase.Playing {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val readyPhase = gameInfo.phase
            assertIs<MafiaPhase.Ready>(readyPhase)
            val job = timerRepository.startTimer(gameInfo.gameOption.turnTime) {
                mafiaGameService.processNextTurn(gameInfo)
            }
            val playingPhase = readyPhase.toPlaying(job)
            gameInfo.phase = playingPhase
            playingPhase
        }

        mafiaPhaseMessenger.broadcastPhase(gameInfo)

        return phase
    }

    private fun getGameInfo(roomId: RoomId): MafiaGameInfo {
        return mafiaGameRepository.getGameInfo(roomId) ?: throw NotFoundRoomException
    }
}
