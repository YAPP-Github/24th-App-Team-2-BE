package com.xorker.xpr.auth.token

import com.xorker.xpr.user.UserId

interface AccessTokenRepository {
    fun getUserIdOrThrow(accessToken: String): UserId

    fun saveAccessToken(userId: UserId, accessToken: String)
}