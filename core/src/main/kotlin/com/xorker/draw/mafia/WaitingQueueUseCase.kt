package com.xorker.draw.mafia

import com.xorker.draw.user.User

interface WaitingQueueUseCase {
    fun enqueue(user: User, locale: String)
    fun remove(user: User, locale: String)
}
