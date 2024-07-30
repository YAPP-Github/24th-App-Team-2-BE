package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaGameInfo
import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.mafia.assertIs
import com.xorker.draw.timer.TimerRepository
import com.xorker.draw.user.UserId
import java.util.*
import org.springframework.stereotype.Component
import kotlin.math.max

@Component
internal class MafiaPhasePlayVoteProcessor(
    private val timerRepository: TimerRepository,
) {

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
                return
            }
        }
        loseStep.invoke()
    }

    private fun findCandidates(players: Map<UserId, Vector<UserId>>): List<Pair<UserId, MutableList<UserId>>> {
        var maximum = 0
        players.forEach { (_, voters) -> maximum = max(maximum, voters.size) }

        val candidates = players.filter { (_, voters) -> voters.size == maximum }
            .toList()

        return candidates
    }
}
