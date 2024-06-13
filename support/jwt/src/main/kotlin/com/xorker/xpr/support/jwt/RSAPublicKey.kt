package com.xorker.xpr.support.jwt

import java.math.BigInteger
import java.security.Key
import java.security.KeyFactory
import java.security.spec.RSAPublicKeySpec
import java.util.*

class RSAPublicKey(
    private val n: String,
    private val e: String,
    private val type: String,
) : SignatureKey {
    override fun getKey(): Key {
        val decoder = Base64.getUrlDecoder()

        val nBytes = decoder.decode(n)
        val eBytes = decoder.decode(e)

        val n = BigInteger(1, nBytes)
        val e = BigInteger(1, eBytes)

        val rsaPublicKeySpec = RSAPublicKeySpec(n, e)

        val keyFactory = KeyFactory.getInstance(type)

        return keyFactory.generatePublic(rsaPublicKeySpec)
    }
}
