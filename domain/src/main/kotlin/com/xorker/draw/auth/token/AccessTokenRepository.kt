package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId
import java.time.Duration

interface AccessTokenRepository {
    fun createAccessToken(userId: UserId, expiredTime: Duration): String
}
