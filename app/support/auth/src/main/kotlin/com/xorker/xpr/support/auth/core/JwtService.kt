package com.xorker.xpr.support.auth.core

import com.xorker.xpr.support.jwt.JwtProvider
import com.xorker.xpr.support.jwt.JwtSecretKey
import com.xorker.xpr.user.UserId
import org.springframework.stereotype.Service

@Service
internal class JwtService : TokenUseCase {
    private val jwtProvider = JwtProvider

    override fun getUserId(token: String): UserId {
        val isValidToken = jwtProvider.validate(token, JwtSecretKey)
        if (!isValidToken) {
            throw JwtValidationFailException
        }
        val subject = jwtProvider.getSubject(token, JwtSecretKey) ?: throw JwtValidationFailException

        return UserId(subject.toLong())
    }
}
