package com.xorker.xpr.support.auth.core

import com.xorker.xpr.support.jwt.JwtProvider
import com.xorker.xpr.support.jwt.JwtSecretKey
import com.xorker.xpr.user.UserId
import org.springframework.stereotype.Service

@Service
internal class JwtService(
    private val jwtProvider: JwtProvider,
    private val jwtSecretKey: JwtSecretKey,
) : TokenUseCase {
    override fun getUserId(token: String): UserId {
        val subject = jwtProvider.getSubject(token, jwtSecretKey) ?: throw JwtValidationFailException

        return UserId(subject.toLong())
    }
}
