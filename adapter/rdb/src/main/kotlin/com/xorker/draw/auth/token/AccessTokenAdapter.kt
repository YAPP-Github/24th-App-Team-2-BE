package com.xorker.draw.auth.token

import com.xorker.draw.support.jwt.JwtProvider
import com.xorker.draw.support.jwt.JwtSecretKey
import com.xorker.draw.user.UserId
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import org.springframework.stereotype.Component

@Component
internal class AccessTokenAdapter(
    private val jwtProvider: JwtProvider,
    private val jwtSecretKey: JwtSecretKey,
) : AccessTokenRepository {
    override fun createAccessToken(userId: UserId, expiredTime: TemporalAmount): String {
        val now = LocalDateTime.now()
        return jwtProvider.generate(
            id = userId.value.toString(),
            expiration = now.plus(expiredTime),
            key = jwtSecretKey,
        )
    }
}
