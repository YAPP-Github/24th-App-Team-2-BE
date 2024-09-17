package com.xorker.draw.mafia

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.user.User
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.stereotype.Component

@Component
internal class MafiaGameWaitingQueueAdapter : MafiaGameWaitingQueueRepository {
    private val waitingQueue: ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> = ConcurrentHashMap()

    override fun size(locale: String): Int {
        return waitingQueue[locale]?.size ?: 0
    }

    override fun enqueue(user: User, locale: String) {
        val queue = waitingQueue.getOrPut(locale) { ConcurrentLinkedQueue() }
        queue.add(user)
    }

    override fun dequeue(locale: String): User {
        val queue = waitingQueue[locale] ?: throw UnSupportedException

        return queue.poll()
    }

    override fun remove(user: User, locale: String) {
        waitingQueue[locale]?.remove(user)
    }
}
