package com.xorker.draw.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.xorker.draw.websocket.WaitingQueueSession
import java.util.Locale
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class FcmService(
    private val messageSource: MessageSource,
) {

    fun quickStart(session: WaitingQueueSession) {
        quickStart(session.locale, session.user.name)
    }

    fun quickStart(localeStr: String, nickname: String) {
        val locale = Locale(localeStr)

        val notification = Notification.builder()
            .setTitle(messageSource.getMessage("fcm.quickstart.title", null, locale))
            .setBody(String.format(messageSource.getMessage("fcm.quickstart.body", null, locale), nickname))
            .build()

        val message = Message.builder()
            .setTopic("${locale}QuickStart")
            .setNotification(notification)
            .build()

        FirebaseMessaging.getInstance()
            .send(message)
    }
}
