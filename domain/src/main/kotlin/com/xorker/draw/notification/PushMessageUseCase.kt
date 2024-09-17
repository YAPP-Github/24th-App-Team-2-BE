package com.xorker.draw.notification

import com.xorker.draw.websocket.WaitingQueueSession

interface PushMessageUseCase {
    fun quickStart(session: WaitingQueueSession)
}
