package com.xorker.draw.websocket.message.response.mafia

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage
import java.time.LocalDateTime

data class MafiaPhaseVoteMessage(
    override val body: MafiaPhaseVoteBody,
) : SessionMessage {
    override val action = ResponseAction.PHASE_VOTE
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseVoteBody(
    val startTime: LocalDateTime,
    val mafiaGameInfo: MafiaGameInfoMessage? = null,
    val draw: List<Map<String, Any>>,
    val players: Map<UserId, List<UserId>>,
)
