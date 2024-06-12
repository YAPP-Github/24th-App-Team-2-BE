package com.xorker.xpr.oauth.apple

import com.xorker.xpr.exception.UnAuthenticationException
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Component

@Component
internal class AppleIdTokenValidator {
    internal fun validateClaims(claims: Claims) {
        if (claims.issuer != ISS || claims.audience != CLIENT_ID) {
            throw UnAuthenticationException
        }
    }

    internal companion object {
        private const val ISS = "https://appleid.apple.com"
        private const val CLIENT_ID = ""
    }
}
