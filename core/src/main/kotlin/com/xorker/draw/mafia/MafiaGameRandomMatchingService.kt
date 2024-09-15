package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.websocket.WaitingQueueSession
import com.xorker.draw.websocket.WaitingQueueSessionEventListener
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRandomMatchingService(
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val mafiaGameWaitingQueueRepository: MafiaGameWaitingQueueRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : WaitingQueueSessionEventListener {

    override fun connectSession(session: WaitingQueueSession) {
        val user = session.user

        mafiaGameUseCase.getGameInfo(user.id)?.let {
            throw InvalidRequestOtherPlayingException
        }

        mafiaGameWaitingQueueRepository.enqueue(3, session)

        mafiaGameMessenger.unicastRandomMatching(user.id)
    }

    override fun exitSession(session: WaitingQueueSession) {
        mafiaGameWaitingQueueRepository.dequeue(session)
    }
}
