package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId

interface MafiaStartGameUseCase {
    fun startMafiaGame(roomId: RoomId)
}
