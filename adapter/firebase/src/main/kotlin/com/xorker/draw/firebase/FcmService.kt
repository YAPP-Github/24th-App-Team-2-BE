package com.xorker.draw.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.xorker.draw.i18n.MessageService
import com.xorker.draw.notification.PushMessageUseCase
import com.xorker.draw.websocket.WaitingQueueSession
import java.util.Locale
import org.springframework.stereotype.Service

@Service
class FcmService(
    private val messageService: MessageService,
) : PushMessageUseCase {

    override fun quickStart(session: WaitingQueueSession) {
        quickStart(session.locale, session.user.name)
    }

    fun quickStart(localeStr: String, nickname: String) {
        val locale = Locale(localeStr)

        val notification = Notification.builder()
            .setTitle(messageService.getMessage("fcm.quickstart.title", locale))
            .setBody(String.format(messageService.getMessage("fcm.quickstart.body", locale), nickname))
            .build()

        val message = Message.builder()
            .setTopic("${locale}QuickStart")
            .setNotification(notification)
            .build()

        FirebaseMessaging.getInstance()
            .send(message)
    }
}
