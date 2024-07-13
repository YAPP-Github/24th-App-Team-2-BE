package com.xorker.draw.mafia

import com.xorker.draw.room.Player
import com.xorker.draw.user.UserId

class MafiaPlayer(
    userId: UserId,
    nickname: String,
    val color: String,
) : Player(userId, nickname)
