package com.xorker.xpr.auth.token

import com.xorker.xpr.user.UserId

interface RefreshTokenRepository {
    fun getUserIdOrThrow(refreshToken: String): UserId

    fun saveRefreshToken(userId: UserId, refreshToken: String)

    fun deleteRefreshToken(userId: UserId)
}