package com.xorker.xpr.oauth.apple

import com.xorker.xpr.exception.OAuthFailureException
import com.xorker.xpr.support.jwt.JwtProvider
import com.xorker.xpr.support.jwt.SignatureKey
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
        val isValidToken = jwtProvider.validate(token, key)
        if (!isValidToken) {
            throw OAuthFailureException
        }
        val isValidClaims = jwtProvider.validateClaimsWith(
            iss = iss,
            aud = clientId,
            token = token,
            key = key,
        )
        if (!isValidClaims) {
            throw OAuthFailureException
        }
        return jwtProvider.getSubject(token, key) ?: throw OAuthFailureException
    }
}
