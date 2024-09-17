package com.xorker.draw.mafia

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.mafia.event.MafiaGameRandomMatchingEvent
import com.xorker.draw.user.User
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
internal class MafiaGameWaitingQueueAdapter(
    private val eventPublisher: ApplicationEventPublisher,
) : MafiaGameWaitingQueueRepository {
    private val waitingQueue: ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> = ConcurrentHashMap()

    override fun enqueue(size: Int, user: User, locale: String) {
        val queue = waitingQueue.getOrPut(locale) { ConcurrentLinkedQueue() }

        queue.add(user)

        synchronized(this) {
            if (queue.size >= size) {
                val players = mutableListOf<User>()

                (0 until size).forEach { _ ->
                    queue.poll().let {
                        players.add(it)
                    }
                }

                val event = MafiaGameRandomMatchingEvent(players, locale)

                eventPublisher.publishEvent(event)
            }
        }
    }

    override fun dequeue(user: User, locale: String) {
        val queue = waitingQueue[locale] ?: throw UnSupportedException

        queue.remove(user)
    }
}
