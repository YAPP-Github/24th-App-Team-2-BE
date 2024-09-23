package com.xorker.draw.mafia

import com.xorker.draw.mafia.dto.DrawRequest
import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User
import com.xorker.draw.user.UserId

interface MafiaGameUseCase {
    fun getGameInfoByUserId(userId: UserId): MafiaGameInfo?
    fun getGameInfoByRoomId(roomId: RoomId?): MafiaGameInfo?
    fun draw(user: User, request: DrawRequest)
    fun nextTurnByUser(user: User)
    fun voteMafia(user: User, targetUserId: UserId)
    fun inferAnswer(user: User, answer: String)
    fun decideAnswer(user: User, answer: String)
    fun react(user: User, reaction: String)
}
