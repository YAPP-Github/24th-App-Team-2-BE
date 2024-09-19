package com.xorker.draw.websocket.message.response.dto.game

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.ResponseAction
import com.xorker.draw.websocket.message.response.SessionMessage

data class MafiaGameVoteStatusMessage(
    override val body: MafiaGameVoteStatusBody,
) : SessionMessage {
    override val action = ResponseAction.VOTE_STATUS
    override val status = SessionMessage.Status.OK
}

data class MafiaGameVoteStatusBody(
    val players: Map<UserId, List<UserId>>,
)
