package com.xorker.draw.oauth.apple

import com.xorker.draw.exception.OAuthFailureException
import com.xorker.draw.support.jwt.JwtProvider
import com.xorker.draw.support.jwt.SignatureKey
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
internal class AppleIdTokenValidator(
    private val jwtProvider: JwtProvider,
) {
    @Value("\${oauth.apple.iss}")
    lateinit var iss: String

    @Value("\${oauth.apple.client-id}")
    lateinit var clientId: String

    internal fun validateAndGetSubject(token: String, key: SignatureKey): String {
        return jwtProvider.validateAndGetSubject(
            token = token,
            key = key,
            iss = iss,
            aud = clientId,
        ) ?: throw OAuthFailureException
    }
}
