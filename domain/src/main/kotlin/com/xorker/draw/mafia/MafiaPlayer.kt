package com.xorker.draw.mafia

import com.xorker.draw.room.Player
import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.SessionId

class MafiaPlayer(
    userId: UserId,
    nickname: String,
    sessionId: SessionId,
    val color: String,
) : Player(userId, nickname, sessionId)
