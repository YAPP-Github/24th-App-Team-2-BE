package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaGameVotePhaseStatusException
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component
import kotlin.math.max

@Component
internal class MafiaVoteService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val timerRepository: TimerRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaVoteUseCase {

    override fun voteMafia(session: Session, targetUserId: UserId) {
        val roomId = session.roomId

        val voter = session.user
        val voterUserId = voter.id

        val gameInfo = mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidMafiaGameVotePhaseStatusException

        val phase = gameInfo.phase as? MafiaPhase.Vote ?: throw InvalidMafiaGameVotePhaseStatusException

        vote(phase.players, voterUserId, targetUserId)

        mafiaGameMessenger.broadcastVoteStatus(gameInfo)
    }

    internal fun playVote(gameInfo: MafiaGameInfo, winStep: () -> Unit, loseStep: () -> Unit): MafiaPhase.Vote {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Playing>(phase)

        val gameOption = gameInfo.gameOption

        val job = timerRepository.startTimer(gameOption.voteTime) {
            processVote(gameInfo, winStep, loseStep)
        }

        val votePhase = phase.toVote(job)
        gameInfo.phase = votePhase

        return votePhase
    }

    private fun processVote(gameInfo: MafiaGameInfo, winStep: () -> Unit, loseStep: () -> Unit) {
        val phase = gameInfo.phase
        assertIs<MafiaPhase.Vote>(phase)

        val mafiaPlayer = phase.mafiaPlayer

        val candidates = findCandidates(phase.players)

        if (candidates.size == 1) {
            val candidate = candidates[0]

            if (candidate.first == mafiaPlayer.userId) {
                winStep.invoke()
            }
        }
        loseStep.invoke()
    }

    @Synchronized
    private fun vote(
        players: ConcurrentHashMap<UserId, MutableList<UserId>>,
        voterUserId: UserId,
        targetUserId: UserId,
    ) {
        players.forEach { player ->
            val userIds = player.value

            if (voterUserId in userIds) {
                userIds.remove(voterUserId)
            }
        }
        players[targetUserId]?.add(voterUserId) ?: InvalidRequestValueException
    }

    private fun findCandidates(players: ConcurrentHashMap<UserId, MutableList<UserId>>): List<Pair<UserId, MutableList<UserId>>> {
        val maximum = 0
        players.forEach { (_, voters) -> max(maximum, voters.size) }

        val candidates = players.filter { (_, voters) -> voters.size == maximum }
            .toList()

        return candidates
    }
}
