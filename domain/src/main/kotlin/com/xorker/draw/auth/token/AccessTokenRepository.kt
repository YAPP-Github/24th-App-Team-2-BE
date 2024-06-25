package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

interface AccessTokenRepository {
    fun getUserIdOrThrow(accessToken: String): UserId

    fun createAccessToken(userId: UserId): String
}
