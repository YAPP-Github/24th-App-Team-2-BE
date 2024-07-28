package com.xorker.draw.mafia

import com.xorker.draw.websocket.Session

interface MafiaKeywordUseCase {
    fun inferAnswer(session: Session, answer: String)
    fun decideAnswer(session: Session, answer: String, nextStep: () -> Unit)
}
