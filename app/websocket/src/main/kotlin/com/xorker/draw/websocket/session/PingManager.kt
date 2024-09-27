package com.xorker.draw.websocket.session

import com.xorker.draw.websocket.SessionEventListener
import com.xorker.draw.websocket.WaitingQueueUseCase
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
internal class PingManager(
    private val sessionManager: SessionManager,
    private val waitingQueueUseCase: WaitingQueueUseCase,
    private val sessionEventListener: List<SessionEventListener>,
) {

    @Scheduled(fixedRate = 1000, timeUnit = TimeUnit.MILLISECONDS)
    fun checkSessionStatus() {
        val sessions = sessionManager.getSessions()

        val now = LocalDateTime.now()

        sessions.forEach { session ->
            val delay = Duration.between(session.ping, now).toSeconds()

            if (delay > ALLOWED_DELAY_TIME) {
                val user = session.user

                sessionManager.unregisterSession(session.id)

                waitingQueueUseCase.remove(user, session.locale)

                sessionEventListener.forEach {
                    it.exitSession(user.id)
                }
            }
        }
    }

    companion object {
        private const val ALLOWED_DELAY_TIME = 2
    }
}
