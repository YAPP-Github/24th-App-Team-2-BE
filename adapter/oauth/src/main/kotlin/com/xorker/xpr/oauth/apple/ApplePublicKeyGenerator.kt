package com.xorker.xpr.oauth.apple

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DatabindException
import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.xpr.exception.OAuthFailureException
import com.xorker.xpr.oauth.apple.dto.ApplePublicKeys
import feign.FeignException
import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.*
import org.springframework.stereotype.Component

@Component
internal class ApplePublicKeyGenerator(
    private val objectMapper: ObjectMapper,
    private val httpClient: AppleFeignClient,
) {
    private val typeReference = object : TypeReference<Map<String, String>>() {}

    internal fun generatePublicKey(token: String): PublicKey {
        val applePublicKeys = getApplePublicKeys()

        val header = parseHeader(token)

        val alg = header["alg"] ?: throw OAuthFailureException
        val kid = header["kid"] ?: throw OAuthFailureException
        val key = applePublicKeys[alg, kid] ?: throw OAuthFailureException

        val decoder = Base64.getUrlDecoder()

        val nBytes = decoder.decode(key.n)
        val eBytes = decoder.decode(key.e)

        val n = BigInteger(1, nBytes)
        val e = BigInteger(1, eBytes)

        val rsaPublicKeySpec = RSAPublicKeySpec(n, e)

        try {
            val keyFactory = KeyFactory.getInstance(key.kty)
            return keyFactory.generatePublic(rsaPublicKeySpec)
        } catch (e: NoSuchAlgorithmException) {
            throw OAuthFailureException
        } catch (e: InvalidKeySpecException) {
            throw OAuthFailureException
        }
    }

    private fun getApplePublicKeys(): ApplePublicKeys {
        try {
            return httpClient.getApplePublicKeys()
        } catch (e: FeignException) {
            throw OAuthFailureException
        }
    }

    private fun parseHeader(token: String): Map<String, String> {
        try {
            val header = token.split(".")[0]
            val decoder = Base64.getUrlDecoder()
            val decoded = String(decoder.decode(header))
            return objectMapper.readValue(decoded, typeReference)
        } catch (e: DatabindException) {
            throw OAuthFailureException
        } catch (e: IndexOutOfBoundsException) {
            throw OAuthFailureException
        }
    }
}
