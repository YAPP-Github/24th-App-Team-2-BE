package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage
import java.time.LocalDateTime

class MafiaPhaseVoteMessage(
    override val body: MafiaPhaseVoteBody,
) : SessionMessage {
    override val action = ResponseAction.PHASE_VOTE
    override val status = SessionMessage.Status.OK
}

data class MafiaPhaseVoteBody(
    val startTime: LocalDateTime,
    val mafiaGameInfo: MafiaGameInfoMessage? = null,
    val players: Map<UserId, List<UserId>>,
)
