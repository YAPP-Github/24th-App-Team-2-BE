package com.xorker.draw.oauth.apple

import com.xorker.draw.exception.OAuthFailureException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.security.PublicKey
import org.springframework.stereotype.Component

@Component
internal class AppleIdTokenValidator {
    internal fun validateAndGetSubject(token: String, key: PublicKey): String {
        try {
            val parser = Jwts.parser()
                .verifyWith(key)
                .build()
            val claims = parser.parseSignedClaims(token).payload
            validateClaims(claims)
            return claims.subject
        } catch (e: JwtException) {
            throw OAuthFailureException
        }
    }

    private fun validateClaims(claims: Claims) {
        if (claims.issuer != ISS || !claims.audience.contains(CLIENT_ID)) {
            throw OAuthFailureException
        }
    }

    // TODO application.yml
    internal companion object {
        private const val ISS = "https://appleid.apple.com"
        private const val CLIENT_ID = ""
    }
}
