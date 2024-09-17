package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOnlyMyTurnException
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.dto.DrawRequest
import com.xorker.draw.mafia.phase.MafiaPhaseInferAnswerProcessor
import com.xorker.draw.mafia.phase.MafiaPhasePlayGameProcessor
import com.xorker.draw.mafia.phase.MafiaPhaseService
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId
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
) : MafiaGameUseCase {

    override fun getGameInfoByUserId(userId: UserId): MafiaGameInfo? {
        return mafiaGameRepository.getGameInfo(userId)
    }

    override fun getGameInfoByRoomId(roomId: RoomId?): MafiaGameInfo? {
        if (roomId == null) return null

        return mafiaGameRepository.getGameInfo(roomId)
    }

    override fun draw(user: User, request: DrawRequest) {
        val gameInfo = user.getGameInfo()
        val phase = gameInfo.phase
        assertTurn(phase, user.id)

        val drawData = phase.drawData.lastOrNull()
        if (drawData != null && drawData.first == user.id) {
            phase.drawData.removeLast()
        }
        phase.drawData.add(Pair(user.id, request.drawData))

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaGameMessenger.broadcastDraw(gameInfo.room.id, request.drawData)
    }

    override fun nextTurnByUser(user: User) {
        val gameInfo = user.getGameInfo()
        val phase = gameInfo.phase
        assertTurn(phase, user.id)

        phase.job.cancel()

        mafiaPhasePlayGameProcessor.processNextTurn(gameInfo) {
            mafiaPhaseService.vote(gameInfo.room.id)
        }
    }

    override fun voteMafia(user: User, targetUserId: UserId) {
        val gameInfo = mafiaGameRepository.getGameInfo(user.id) ?: throw InvalidRequestValueException

        val phase = gameInfo.phase
        assertIs<MafiaPhase.Vote>(phase)

        vote(phase.players, user, targetUserId)

        mafiaGameMessenger.broadcastVoteStatus(gameInfo)
    }

    override fun inferAnswer(user: User, answer: String) {
        val gameInfo = mafiaGameRepository.getGameInfo(user.id) ?: throw InvalidRequestValueException

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        validateIsMafia(user, phase.mafiaPlayer)

        phase.answer = answer

        mafiaGameMessenger.broadcastAnswer(gameInfo, answer)
    }

    override fun decideAnswer(user: User, answer: String) {
        val gameInfo = user.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        phase.answer = answer

        val job = phase.job
        job.cancel()

        mafiaPhaseInferAnswerProcessor.processInferAnswer(gameInfo) {
            mafiaPhaseService.endGame(gameInfo.room.id)
        }
    }

    override fun react(user: User, reaction: String) {
        val gameInfo = user.getGameInfo()

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

    private fun User.getGameInfo(): MafiaGameInfo =
        mafiaGameRepository.getGameInfo(this.id) ?: throw InvalidRequestValueException

    @OptIn(ExperimentalContracts::class)
    private fun assertTurn(phase: MafiaPhase, userId: UserId) {
        contract {
            returns() implies (phase is MafiaPhase.Playing)
        }
        if (phase !is MafiaPhase.Playing) throw InvalidRequestValueException
        if (phase.getPlayerTurn(userId) != phase.turn) throw InvalidRequestOnlyMyTurnException
    }
}
