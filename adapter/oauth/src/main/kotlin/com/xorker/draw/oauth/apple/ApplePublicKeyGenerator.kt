package com.xorker.draw.oauth.apple

import com.xorker.draw.exception.OAuthFailureException
import com.xorker.draw.oauth.apple.dto.ApplePublicKeys
import com.xorker.draw.support.jwt.JwtProvider
import com.xorker.draw.support.jwt.RSAPublicKey
import com.xorker.draw.support.jwt.SignatureKey
import feign.FeignException
import org.springframework.stereotype.Component

@Component
internal class ApplePublicKeyGenerator(
    private val httpClient: AppleFeignClient,
    private val jwtProvider: JwtProvider,
) {
    internal fun generatePublicKey(token: String): SignatureKey {
        val applePublicKeys = getApplePublicKeys()

        val header = jwtProvider.parseHeader(token) ?: throw OAuthFailureException

        val alg = header["alg"] ?: throw OAuthFailureException
        val kid = header["kid"] ?: throw OAuthFailureException
        val key = applePublicKeys[alg, kid] ?: throw OAuthFailureException

        return RSAPublicKey(key.n, key.e, key.kty)
    }

    private fun getApplePublicKeys(): ApplePublicKeys {
        try {
            return httpClient.getApplePublicKeys()
        } catch (e: FeignException) {
            throw OAuthFailureException
        }
    }
}
