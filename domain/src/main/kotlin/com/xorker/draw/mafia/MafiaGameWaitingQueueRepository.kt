package com.xorker.draw.mafia

import com.xorker.draw.user.User

interface MafiaGameWaitingQueueRepository {
    fun size(locale: String): Int
    fun enqueue(user: User, locale: String)
    fun dequeue(locale: String): User
    fun remove(user: User, locale: String)
}
