package com.xorker.draw.websocket

interface SessionEventListener {
    fun connectSession(session: Session, request: SessionInitializeRequest)
    fun connectSession(session: Session, locale: String)
    fun disconnectSession(session: Session)
    fun exitSession(session: Session)
}
