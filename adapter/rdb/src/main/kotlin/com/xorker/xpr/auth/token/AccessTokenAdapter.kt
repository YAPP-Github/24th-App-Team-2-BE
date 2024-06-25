package com.xorker.xpr.auth.token

import com.xorker.xpr.support.jwt.JwtProvider
import com.xorker.xpr.support.jwt.JwtSecretKey
import com.xorker.xpr.user.UserId
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
internal class AccessTokenAdapter(
    private val jwtProvider: JwtProvider,
    private val jwtSecretKey: JwtSecretKey,
) : AccessTokenQueryRepository, AccessTokenCommandRepository {
    override fun getUserIdOrThrow(accessToken: String): UserId {
        return UserId(0L) // TODO 추후 메서드 필요 시 변경
    }

    override fun createAccessToken(userId: UserId): String {
        val now = LocalDateTime.now()
        return jwtProvider.generate(
            id = userId.value.toString(),
            expiration = now.plusMinutes(10),
            key = jwtSecretKey,
        )
    }
}
