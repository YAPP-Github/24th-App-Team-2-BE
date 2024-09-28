package com.xorker.draw.mafia.phase

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.event.mafia.MafiaGameInfoEventProducer
import com.xorker.draw.exception.NotFoundRoomException
import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaGameRepository
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.assert
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import org.springframework.stereotype.Service

@Service
internal class MafiaPhaseService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaPhaseStartGameProcessor: MafiaPhaseStartGameProcessor,
    private val mafiaPhasePlayGameProcessor: MafiaPhasePlayGameProcessor,
    private val mafiaPhasePlayVoteProcessor: MafiaPhasePlayVoteProcessor,
    private val mafiaPhaseInferAnswerProcessor: MafiaPhaseInferAnswerProcessor,
    private val mafiaPhaseEndGameProcessor: MafiaPhaseEndGameProcessor,
    private val mafiaGameInfoEventProducer: MafiaGameInfoEventProducer,
) : MafiaPhaseUseCase {

    override fun startGame(user: User): MafiaPhase.Ready {
        val gameInfo = mafiaGameRepository.getGameInfo(user.id) ?: throw InvalidRequestValueException
        return startGame(gameInfo)
    }

    override fun startGame(roomId: RoomId): MafiaPhase.Ready {
        val gameInfo = getGameInfo(roomId)
        return startGame(gameInfo)
    }

    private fun startGame(gameInfo: MafiaGameInfo): MafiaPhase.Ready {
        val roomId = gameInfo.room.id
        val phase = synchronized(gameInfo) {
            assertIs<MafiaPhase.Wait>(gameInfo.phase)
            mafiaPhaseStartGameProcessor.startMafiaGame(gameInfo) {
                playGame(roomId)
            }
        }

        mafiaGameInfoEventProducer.changePhase(gameInfo)

        return phase
    }

    override fun playGame(roomId: RoomId): MafiaPhase.Playing {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val readyPhase = gameInfo.phase
            assertIs<MafiaPhase.Ready>(readyPhase)
            mafiaPhasePlayGameProcessor.playMafiaGame(gameInfo) {
                vote(roomId)
            }
        }

        mafiaGameInfoEventProducer.changePhase(gameInfo)

        return phase
    }

    override fun vote(roomId: RoomId): MafiaPhase.Vote {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val playingPhase = gameInfo.phase
            assertIs<MafiaPhase.Playing>(playingPhase)
            mafiaPhasePlayVoteProcessor.playVote(
                gameInfo,
                {
                    interAnswer(roomId)
                },
                {
                    endGame(roomId)
                },
            )
        }

        mafiaGameInfoEventProducer.changePhase(gameInfo)

        return phase
    }

    override fun interAnswer(roomId: RoomId): MafiaPhase.InferAnswer {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val votePhase = gameInfo.phase
            assertIs<MafiaPhase.Vote>(votePhase)
            mafiaPhaseInferAnswerProcessor.playInferAnswer(gameInfo) {
                endGame(roomId)
            }
        }

        mafiaGameInfoEventProducer.changePhase(gameInfo)

        return phase
    }

    override fun endGame(roomId: RoomId): MafiaPhase.End {
        val gameInfo = getGameInfo(roomId)

        val phase = synchronized(gameInfo) {
            val votePhase = gameInfo.phase
            assert<MafiaPhase.Vote, MafiaPhase.InferAnswer>(votePhase)
            mafiaPhaseEndGameProcessor.endGame(gameInfo)
        }

        mafiaGameInfoEventProducer.changePhase(gameInfo)

        return phase
    }

    private fun getGameInfo(roomId: RoomId): MafiaGameInfo {
        return mafiaGameRepository.getGameInfo(roomId) ?: throw NotFoundRoomException
    }
}
