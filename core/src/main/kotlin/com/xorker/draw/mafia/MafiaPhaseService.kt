package com.xorker.draw.mafia

import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.room.RoomId
import org.springframework.stereotype.Service

@Service
internal class MafiaPhaseService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val startGameService: MafiaStartGameService,
    private val mafiaGameService: MafiaGameService,
    private val mafiaVoteService: MafiaVoteService,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
) : MafiaPhaseUseCase {

    override fun startGame(roomId: RoomId): MafiaPhase.Ready {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            assertIs<MafiaPhase.Wait>(gameInfo.phase)
            startGameService.startMafiaGame(gameInfo) {
                playGame(roomId)
            }
        }

        mafiaPhaseMessenger.broadcastPhase(gameInfo)

        return phase
    }

    override fun playGame(roomId: RoomId): MafiaPhase.Playing {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val readyPhase = gameInfo.phase
            assertIs<MafiaPhase.Ready>(readyPhase)
            mafiaGameService.playMafiaGame(gameInfo) {
                vote(roomId)
            }
        }

        mafiaPhaseMessenger.broadcastPhase(gameInfo)

        return phase
    }

    override fun vote(roomId: RoomId): MafiaPhase.Vote {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val playingPhase = gameInfo.phase
            assertIs<MafiaPhase.Playing>(playingPhase)
            mafiaVoteService.playVote(
                gameInfo,
                {
                    interAnswer(roomId)
                },
                {
                    endGame(roomId)
                },
            )
        }

        mafiaPhaseMessenger.broadcastPhase(gameInfo)

        return phase
    }

    override fun interAnswer(roomId: RoomId): MafiaPhase.InferAnswer {
        TODO()
    }

    override fun endGame(roomId: RoomId): MafiaPhase.End {
        // TODO
        return MafiaPhase.End()
    }

    private fun getGameInfo(roomId: RoomId): MafiaGameInfo {
        return mafiaGameRepository.getGameInfo(roomId) ?: throw NotFoundRoomException
    }
}
