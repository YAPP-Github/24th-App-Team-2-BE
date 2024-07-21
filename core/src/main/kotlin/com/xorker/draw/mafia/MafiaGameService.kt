package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.mafia.dto.DrawRequest
import com.xorker.draw.websocket.Session
import org.springframework.stereotype.Component

@Component
internal class MafiaGameService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaGameUseCase {

    override fun draw(session: Session, request: DrawRequest) {
        val gameInfo = mafiaGameRepository.getGameInfo(session.roomId) ?: throw InvalidRequestValueException
        val phase = gameInfo.phase
        if (phase !is MafiaPhase.Playing) throw InvalidRequestValueException

        val drawData = phase.drawData.lastOrNull()
        if (drawData != null && drawData.first == session.user.id) {
            phase.drawData.removeLast()
        }
        phase.drawData.add(Pair(session.user.id, request.drawData))

        mafiaGameRepository.saveGameInfo(gameInfo)
        mafiaGameMessenger.broadcastDraw(gameInfo)
    }
}
