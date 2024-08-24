package com.xorker.draw.mafia

import com.xorker.draw.websocket.WaitingQueueSession

interface MafiaGameWaitingQueueRepository {
    fun enqueue(size: Int, session: WaitingQueueSession)
    fun dequeue(session: WaitingQueueSession)
}
