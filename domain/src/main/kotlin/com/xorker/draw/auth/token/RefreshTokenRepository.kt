package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

interface RefreshTokenRepository {
    fun getUserIdOrThrow(refreshToken: String): UserId

    fun createRefreshToken(userId: UserId): String

    fun deleteRefreshToken(userId: UserId)
}
