package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestOtherPlayingException
import com.xorker.draw.mafia.phase.MafiaPhaseUseCase
import com.xorker.draw.notification.PushMessageUseCase
import com.xorker.draw.notify.NotifyRepository
import com.xorker.draw.notify.NotifyType
import com.xorker.draw.user.User
import org.springframework.stereotype.Service

@Service
internal class MafiaGameRandomMatchingService(
    private val mafiaGameUseCase: MafiaGameUseCase,
    private val mafiaGameWaitingQueueRepository: MafiaGameWaitingQueueRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
    private val mafiaPhaseUseCase: MafiaPhaseUseCase,
    private val mafiaGameRoomService: MafiaGameRoomService,
    private val pushMessageUseCase: PushMessageUseCase,
    private val notifyRepository: NotifyRepository,
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

                newGameStart(players, locale)
            } else {
                notifyRepository.notifyMessage(NotifyType.DiscordRandomMatchingNotifyType(user.name, locale))
                pushMessageUseCase.quickStart(locale, user.name)
            }
        }
    }

    private fun newGameStart(players: List<User>, locale: String) {
        val roomId = mafiaGameRoomService.generateRoomId()

        players.forEach { user ->
            mafiaGameRoomService.connectGame(user, roomId, locale, true)
        }

        mafiaPhaseUseCase.startGame(roomId)
    }

    override fun remove(user: User, locale: String) {
        mafiaGameWaitingQueueRepository.remove(user, locale)
    }

    companion object {
        private const val MINIMUM_GAME_START: Int = 3
    }
}
