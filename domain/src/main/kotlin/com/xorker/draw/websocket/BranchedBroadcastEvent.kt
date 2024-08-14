package com.xorker.draw.websocket

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.UserId

data class BranchedBroadcastEvent(
    val roomId: RoomId,
    val branched: Set<UserId>,
    val message: SessionMessage,
    val branchedMessage: SessionMessage,
)
