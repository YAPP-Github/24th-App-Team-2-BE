package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidMafiaGameVotePhaseStatusException
import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session
import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
internal class MafiaVoteService(
    private val mafiaGameRepository: MafiaGameRepository,
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

    @Synchronized
    private fun vote(
        players: ConcurrentHashMap<UserId, MutableSet<UserId>>,
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
}
