package com.xorker.draw.auth.token

import com.xorker.draw.support.jwt.JwtProvider
import com.xorker.draw.support.jwt.JwtSecretKey
import com.xorker.draw.user.UserId
import org.springframework.stereotype.Service

@Service
internal class JwtService(
    private val jwtProvider: JwtProvider,
    private val jwtSecretKey: JwtSecretKey,
) : TokenUseCase {
    override fun getUserId(token: String): UserId? {
        val subject = jwtProvider.validateAndGetSubject(token, jwtSecretKey) ?: return null

        return UserId(subject.toLong())
    }
}
