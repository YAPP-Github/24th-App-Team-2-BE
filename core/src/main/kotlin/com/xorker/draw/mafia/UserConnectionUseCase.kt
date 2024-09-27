package com.xorker.draw.mafia

import com.xorker.draw.room.RoomId
import com.xorker.draw.user.User

interface UserConnectionUseCase {
    fun connectUser(user: User, roomId: RoomId?, locale: String)
    fun exitUser(user: User)
    fun disconnectUser(user: User)
}
