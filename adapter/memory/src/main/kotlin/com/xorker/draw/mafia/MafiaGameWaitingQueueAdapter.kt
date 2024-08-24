package com.xorker.draw.mafia

import com.xorker.draw.exception.UnSupportedException
import com.xorker.draw.mafia.event.MafiaGameRandomMatchingEvent
import com.xorker.draw.websocket.WaitingQueueSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
internal class MafiaGameWaitingQueueAdapter(
    private val eventPublisher: ApplicationEventPublisher,
) : MafiaGameWaitingQueueRepository {
    private val koWaitingQueue: ConcurrentLinkedQueue<WaitingQueueSession> = ConcurrentLinkedQueue()
    private val enWaitingQueue: ConcurrentLinkedQueue<WaitingQueueSession> = ConcurrentLinkedQueue()
    private val waitingQueue: ConcurrentHashMap<String, ConcurrentLinkedQueue<WaitingQueueSession>> = ConcurrentHashMap()
    private val lock = Object()

    init {
        waitingQueue["ko"] = koWaitingQueue
        waitingQueue["en"] = enWaitingQueue
    }

    override fun enqueue(size: Int, session: WaitingQueueSession) {
        val queue = waitingQueue[session.locale] ?: throw UnSupportedException

        queue.add(session)

        synchronized(lock) {
            if (queue.size >= size) {
                val players = mutableListOf<WaitingQueueSession>()

                (0 until size).forEach { _ ->
                    queue.poll().let {
                        players.add(it)
                    }
                }

                val event = MafiaGameRandomMatchingEvent(players)

                eventPublisher.publishEvent(event)
            }
        }
    }

    override fun dequeue(session: WaitingQueueSession) {
        val queue = waitingQueue[session.locale] ?: throw UnSupportedException

        queue.remove(session)
    }
}
