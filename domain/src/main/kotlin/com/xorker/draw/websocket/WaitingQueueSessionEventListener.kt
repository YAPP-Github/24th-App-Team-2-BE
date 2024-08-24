package com.xorker.draw.websocket

interface WaitingQueueSessionEventListener {
    fun connectSession(session: WaitingQueueSession)
    fun exitSession(session: WaitingQueueSession)
}
