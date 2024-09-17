package com.xorker.draw.mafia

import com.xorker.draw.user.User

interface MafiaGameWaitingQueueRepository {
    fun enqueue(size: Int, user: User, locale: String)
    fun dequeue(user: User, locale: String)
}
