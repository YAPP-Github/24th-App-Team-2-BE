package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.mafia.event.MafiaGameRandomMatchingEvent
import com.xorker.draw.notification.PushMessageUseCase
import com.xorker.draw.user.User
import com.xorker.draw.websocket.WaitingQueueUseCase
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRandomMatchingService(
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val mafiaGameWaitingQueueRepository: MafiaGameWaitingQueueRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val pushMessageUseCase: PushMessageUseCase,
    private val eventPublisher: ApplicationEventPublisher,
) : WaitingQueueUseCase {

    override fun enqueue(user: User, locale: String) {
        val gameInfo = mafiaGameUseCase.getGameInfoByUserId(user.id)
        if (gameInfo != null) throw InvalidRequestOtherPlayingException

        mafiaGameWaitingQueueRepository.enqueue(user, locale)
        mafiaGameMessenger.unicastRandomMatching(user.id)

        synchronized(this) {
            val size = mafiaGameWaitingQueueRepository.size(locale)

            if (size >= MINIMUM_GAME_START) {
                val players = mutableListOf<User>()

                (0 until size).forEach { _ ->
                    val player = mafiaGameWaitingQueueRepository.dequeue(locale)
                    players.add(player)
                }

                val event = MafiaGameRandomMatchingEvent(players, locale)

                eventPublisher.publishEvent(event)
            } else {
                pushMessageUseCase.quickStart(locale, user.name)
            }
        }
    }

    override fun remove(user: User, locale: String) {
        mafiaGameWaitingQueueRepository.remove(user, locale)
    }

    companion object {
        private const val MINIMUM_GAME_START: Int = 3
    }
}
