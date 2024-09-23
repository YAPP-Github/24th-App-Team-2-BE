package com.xorker.draw.websocket.session

import com.xorker.draw.websocket.SessionEventListener
import com.xorker.draw.websocket.WaitingQueueUseCase
import java.util.concurrent.TimeUnit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
internal class PingManager(
    private val sessionManager: SessionManager,
    private val waitingQueueUseCase: WaitingQueueUseCase,
    private val sessionEventListener: List<SessionEventListener>,
) {

    @Scheduled(fixedRate = 3000, timeUnit = TimeUnit.MILLISECONDS)
    fun checkSessionStatus() {
        val sessions = sessionManager.getSessions()

        sessions.forEach { session ->
            if (session.ping.not()) {
                val user = session.user

                sessionManager.unregisterSession(session.id)

                waitingQueueUseCase.remove(user, session.locale)

                sessionEventListener.forEach {
                    it.exitSession(user.id)
                }
            } else {
                session.ping = false
            }
        }
    }
}
