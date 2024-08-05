package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOnlyMyTurnException
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.dto.DrawRequest
import com.xorker.draw.mafia.phase.MafiaPhaseInferAnswerProcessor
import com.xorker.draw.mafia.phase.MafiaPhasePlayGameProcessor
import com.xorker.draw.mafia.phase.MafiaPhaseService
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session
import java.util.*
import org.springframework.stereotype.Service
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Service
internal class MafiaGameService(
    private val mafiaPhaseService: MafiaPhaseService,
    private val mafiaPhasePlayGameProcessor: MafiaPhasePlayGameProcessor,
    private val mafiaPhaseInferAnswerProcessor: MafiaPhaseInferAnswerProcessor,
    private val mafiaGameRoomService: MafiaGameRoomService,
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaPhaseMessenger: MafiaPhaseMessenger,
) : MafiaGameUseCase {

    override fun getGameInfo(userId: UserId): MafiaGameInfo? {
        return mafiaGameRepository.getGameInfo(userId)
    }

    override fun draw(session: Session, request: DrawRequest) {
        val gameInfo = session.getGameInfo()
        val phase = gameInfo.phase
        assertTurn(phase, session.user.id)

        val drawData = phase.drawData.lastOrNull()
        if (drawData != null && drawData.first == session.user.id) {
            phase.drawData.removeLast()
        }
        phase.drawData.add(Pair(session.user.id, request.drawData))

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaGameMessenger.broadcastDraw(gameInfo.room.id, request.drawData)
    }

    override fun nextTurnByUser(session: Session) {
        val gameInfo = session.getGameInfo()
        val phase = gameInfo.phase
        assertTurn(phase, session.user.id)

        phase.timerJob.cancel()

        mafiaPhasePlayGameProcessor.processNextTurn(gameInfo) {
            mafiaPhaseService.vote(gameInfo.room.id)
        }
    }

    override fun voteMafia(session: Session, targetUserId: UserId) {
        val gameInfo = session.getGameInfo()

        val voter = session.user

        val phase = gameInfo.phase
        assertIs<MafiaPhase.Vote>(phase)

        vote(phase.players, voter, targetUserId)

        mafiaGameMessenger.broadcastVoteStatus(gameInfo)
    }

    override fun inferAnswer(session: Session, answer: String) {
        val gameInfo = session.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        validateIsMafia(session.user, phase.mafiaPlayer)

        phase.answer = answer

        mafiaGameMessenger.broadcastAnswer(gameInfo, answer)
    }

    override fun decideAnswer(session: Session, answer: String) {
        val gameInfo = session.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        phase.answer = answer

        val job = phase.job
        job.cancel()

        mafiaPhaseInferAnswerProcessor.processInferAnswer(gameInfo) {
            mafiaPhaseService.endGame(gameInfo.room.id)
        }
    }

    override fun gameAgain(session: Session) {
        val gameInfo = session.getGameInfo()
        val room = gameInfo.room

        val phase = gameInfo.phase
        assert<MafiaPhase.End, MafiaPhase.Wait>(phase)

        val user = session.user
        val mafiaPlayer = MafiaPlayer(user.id, user.name, mafiaGameRoomService.generateColor(gameInfo))

        if (gameInfo.phase is MafiaPhase.End) {
            room.clear()
            room.owner = mafiaPlayer
            gameInfo.phase = MafiaPhase.Wait
        }

        room.add(mafiaPlayer)

        mafiaGameRepository.saveGameInfo(gameInfo)

        mafiaPhaseMessenger.unicastPhase(user.id, gameInfo)
        mafiaGameMessenger.broadcastPlayerList(gameInfo)
    }

    private fun vote(
        players: Map<UserId, Vector<UserId>>,
        voter: User,
        targetUserId: UserId,
    ) {
        synchronized(voter) {
            val voterUserId = voter.id

            players.forEach { player ->
                val userIds = player.value

                if (voterUserId in userIds) {
                    userIds.remove(voterUserId)
                }
            }
            players[targetUserId]?.add(voterUserId) ?: InvalidRequestValueException
        }
    }

    private fun validateIsMafia(player: User, mafiaPlayer: MafiaPlayer) {
        if (player.id != mafiaPlayer.userId) {
            throw InvalidRequestValueException
        }
    }

    private fun Session.getGameInfo(): MafiaGameInfo =
        mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidRequestValueException

    @OptIn(ExperimentalContracts::class)
    private fun assertTurn(phase: MafiaPhase, userId: UserId) {
        contract {
            returns() implies (phase is MafiaPhase.Playing)
        }
        if (phase !is MafiaPhase.Playing) throw InvalidRequestValueException
        if (phase.getPlayerTurn(userId) == phase.turn) throw InvalidRequestOnlyMyTurnException
    }
}
