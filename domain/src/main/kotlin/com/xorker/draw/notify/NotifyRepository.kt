package com.xorker.draw.notify

interface NotifyRepository {
    fun notifyMessage(notifyType: NotifyType)
}
