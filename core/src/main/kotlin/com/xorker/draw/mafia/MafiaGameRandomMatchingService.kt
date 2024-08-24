package com.xorker.draw.mafia

import com.xorker.draw.websocket.WaitingQueueSession
import com.xorker.draw.websocket.WaitingQueueSessionEventListener
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRandomMatchingService(
    private val mafiaGameWaitingQueueRepository: MafiaGameWaitingQueueRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : WaitingQueueSessionEventListener {

    override fun connectSession(session: WaitingQueueSession) {
        mafiaGameWaitingQueueRepository.enqueue(4, session)

        val user = session.user

        mafiaGameMessenger.unicastRandomMatching(user.id)
    }

    override fun exitSession(session: WaitingQueueSession) {
        mafiaGameWaitingQueueRepository.dequeue(session)
    }
}
