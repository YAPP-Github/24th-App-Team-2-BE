package com.xorker.xpr.auth.token

import com.xorker.xpr.user.UserId

interface RefreshTokenRepository {
    fun getUserIdOrThrow(refreshToken: String): UserId

    fun createRefreshToken(userId: UserId): String

    fun deleteRefreshToken(userId: UserId)
}