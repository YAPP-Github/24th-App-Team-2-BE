package com.xorker.draw.mafia

import com.xorker.draw.user.User

interface UserConnectionUseCase {
    fun exitUser(user: User)
    fun disconnectUser(user: User)
}
