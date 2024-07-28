package com.xorker.draw.websocket.message.response.dto

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.ResponseAction
import com.xorker.draw.websocket.SessionMessage

class MafiaVoteStatusMessage(
    override val body: MafiaVoteStatusBody,
) : SessionMessage {
    override val action = ResponseAction.VOTE_STATUS
    override val status = SessionMessage.Status.OK
}

data class MafiaVoteStatusBody(
    val players: Map<UserId, Set<UserId>>,
)
