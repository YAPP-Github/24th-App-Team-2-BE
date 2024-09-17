package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.user.User
import com.xorker.draw.websocket.WaitingQueueSessionEventListener
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRandomMatchingService(
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val mafiaGameWaitingQueueRepository: MafiaGameWaitingQueueRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : WaitingQueueSessionEventListener {

    override fun connectSession(user: User, locale: String) {
        mafiaGameUseCase.getGameInfoByUserId(user.id)?.let {
            throw InvalidRequestOtherPlayingException
        }

        mafiaGameWaitingQueueRepository.enqueue(3, user, locale)

        mafiaGameMessenger.unicastRandomMatching(user.id)
    }

    override fun exitSession(user: User, locale: String) {
        mafiaGameWaitingQueueRepository.dequeue(user, locale)
    }
}
