package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId

interface MafiaPhaseUseCase {
    /**
     * MafiaPhase.Wait -> MafiaPhase.Ready
     */
    fun startGame(roomId: RoomId): MafiaPhase.Ready
}
