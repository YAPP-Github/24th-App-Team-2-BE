package com.xorker.draw.mafia

import com.xorker.draw.user.UserId
import com.xorker.draw.websocket.Session

interface MafiaVoteUseCase {
    fun voteMafia(session: Session, targetUserId: UserId)
}
