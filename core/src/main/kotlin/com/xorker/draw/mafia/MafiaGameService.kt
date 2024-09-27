package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOnlyMyTurnException
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.dto.DrawRequest
import com.xorker.draw.mafia.phase.MafiaPhaseInferAnswerProcessor
import com.xorker.draw.mafia.phase.MafiaPhasePlayGameProcessor
import com.xorker.draw.mafia.phase.MafiaPhaseService
import com.xorker.draw.room.RoomId
import com.xorker.draw.timer.TimerRepository
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
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val timerRepository: TimerRepository,
) : MafiaGameUseCase {

    override fun getGameInfo(userId: UserId): MafiaGameInfo? {
        return mafiaGameRepository.getGameInfo(userId)
    }

    override fun getGameInfo(roomId: RoomId?): MafiaGameInfo? {
        if (roomId == null) return null

        return mafiaGameRepository.getGameInfo(roomId)
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

        val room = gameInfo.room

        timerRepository.cancelTimer(room.id)

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

        val room = gameInfo.room

        timerRepository.cancelTimer(room.id)

        mafiaPhaseInferAnswerProcessor.processInferAnswer(gameInfo) {
            mafiaPhaseService.endGame(gameInfo.room.id)
        }
    }

    override fun react(session: Session, reaction: String) {
        val gameInfo = session.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val mafiaReaction = MafiaReactionType.of(reaction)

        mafiaGameMessenger.broadcastReaction(gameInfo, mafiaReaction)
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
        if (phase.getPlayerTurn(userId) != phase.turn) throw InvalidRequestOnlyMyTurnException
    }
}
