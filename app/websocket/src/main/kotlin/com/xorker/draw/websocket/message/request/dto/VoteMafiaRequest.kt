package com.xorker.draw.websocket.message.request.dto

import com.xorker.draw.user.UserId

data class VoteMafiaRequest(
    val userId: UserId,
)
