package com.xorker.xpr.oauth.apple

import com.xorker.xpr.exception.UnAuthenticationException
import com.xorker.xpr.oauth.apple.dto.ApplePublicKey
import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.*
import org.springframework.stereotype.Component

@Component
internal class PublicKeyGenerator {
    internal fun generatePublicKey(key: ApplePublicKey): PublicKey {
        val decoder = Base64.getUrlDecoder()

        val nBytes = decoder.decode(key.n)
        val eBytes = decoder.decode(key.e)

        val n = BigInteger(1, nBytes)
        val e = BigInteger(1, eBytes)

        val publicKeySpec = RSAPublicKeySpec(n, e)

        try {
            val keyFactory = KeyFactory.getInstance(key.kty)
            return keyFactory.generatePublic(publicKeySpec)
        } catch (e: NoSuchAlgorithmException) {
            throw UnAuthenticationException
        } catch (e: InvalidKeySpecException) {
            throw UnAuthenticationException
        }
    }
}
