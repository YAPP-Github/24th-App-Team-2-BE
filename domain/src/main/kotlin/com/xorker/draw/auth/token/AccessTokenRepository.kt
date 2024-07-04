package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

interface AccessTokenRepository {
    fun createAccessToken(userId: UserId): String
}
