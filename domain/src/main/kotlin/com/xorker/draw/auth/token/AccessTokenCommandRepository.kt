package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

interface AccessTokenCommandRepository {
    fun createAccessToken(userId: UserId): String
}
