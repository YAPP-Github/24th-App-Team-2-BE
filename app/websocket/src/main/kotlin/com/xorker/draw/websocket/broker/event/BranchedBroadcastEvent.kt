package com.xorker.draw.websocket.broker.event

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.message.response.SessionMessage

data class BranchedBroadcastEvent(
    val roomId: RoomId,
    val branched: Set<UserId>,
    val message: SessionMessage,
    val branchedMessage: SessionMessage,
)
