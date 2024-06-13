package com.xorker.xpr.auth.token

import com.xorker.xpr.user.UserId

interface AccessTokenCommandRepository {
    fun createAccessToken(userId: UserId): String
}
