package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId
import java.time.temporal.TemporalAmount

interface AccessTokenRepository {
    fun createAccessToken(userId: UserId, expiredTime: TemporalAmount): String
}
