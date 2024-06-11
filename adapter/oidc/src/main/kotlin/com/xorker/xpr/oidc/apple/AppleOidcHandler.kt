package com.xorker.xpr.oidc.apple

import com.xorker.xpr.exception.UnAuthenticationException
import com.xorker.xpr.oidc.apple.dto.ApplePublicKeys
import feign.FeignException
import org.springframework.stereotype.Component

@Component
internal class AppleOidcHandler(
    private val httpClient: AppleFeignClient,
    private val parser: AppleIdTokenParser,
    private val matcher: ApplePublicKeyMatcher,
    private val keyGenerator: PublicKeyGenerator,
    private val validator: AppleIdTokenValidator,
) {
    internal fun getPlatformUserId(token: String): String {
        val applePublicKeys = getApplePublicKeys()

        val header = parser.parseHeader(token)
        val applePublicKey = matcher.getMatchedApplePublicKey(token, applePublicKeys.keys, header)

        val publicKey = keyGenerator.generatePublicKey(applePublicKey)

        val claims = parser.getClaims(token, publicKey)
        validator.validateClaims(claims)
        return claims.subject
    }

    private fun getApplePublicKeys(): ApplePublicKeys {
        try {
            return httpClient.getApplePublicKeys()
        } catch (e: FeignException) {
            throw UnAuthenticationException
        }
    }
}
