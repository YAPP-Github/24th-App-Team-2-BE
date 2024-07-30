package com.xorker.draw.mafia.phase

import com.xorker.draw.mafia.MafiaPhase
import com.xorker.draw.room.RoomId

interface MafiaPhaseUseCase {
    /**
     * MafiaPhase.Wait -> MafiaPhase.Ready
     */
    fun startGame(roomId: RoomId): MafiaPhase.Ready

    /**
     * MafiaPhase.Ready -> MafiaPhase.Playing
     */
    fun playGame(roomId: RoomId): MafiaPhase.Playing

    /**
     * MafiaPhase.Playing -> MafiaPhase.Vote
     */
    fun vote(roomId: RoomId): MafiaPhase.Vote

    /**
     * MafiaPhase.Vote -> MafiaPhase.InferAnswer
     */
    fun interAnswer(roomId: RoomId): MafiaPhase.InferAnswer

    /**
     * MafiaPhase.InferAnswer -> MafiaPhase.End
     */
    fun endGame(roomId: RoomId): MafiaPhase.End
}
