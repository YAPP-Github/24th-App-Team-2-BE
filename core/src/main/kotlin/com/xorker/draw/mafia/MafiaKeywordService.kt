package com.xorker.draw.mafia

import com.xorker.draw.exception.InvalidRequestValueException
import com.xorker.draw.websocket.Session
import org.springframework.stereotype.Service

@Service
internal class MafiaKeywordService(
    private val mafiaGameRepository: MafiaGameRepository,
    private val mafiaGameMessenger: MafiaGameMessenger,
) : MafiaKeywordUseCase {

    override fun inferAnswer(session: Session, answer: String) {
        val gameInfo = session.getGameInfo()

        val phase = gameInfo.phase
        assertIs<MafiaPhase.InferAnswer>(phase)

        phase.answer = answer

        mafiaGameMessenger.broadcastAnswer(gameInfo, answer)
    }

    private fun Session.getGameInfo(): MafiaGameInfo =
        mafiaGameRepository.getGameInfo(roomId) ?: throw InvalidRequestValueException
}
