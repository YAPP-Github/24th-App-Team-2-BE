package com.xorker.xpr.support.jwt

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DatabindException
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import java.security.PublicKey
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*
import javax.crypto.SecretKey

class JwtProvider {
    private val objectMapper = ObjectMapper()
    private val typeReference = object : TypeReference<Map<String, String>>() {}

    fun generate(id: String, expiration: LocalDateTime, key: SignatureKey): String {
        return Jwts.builder()
            .issuedAt(now().toDate())
            .expiration(expiration.toDate())
            .subject(id)
            .signWith(key.key)
            .compact()
    }

    fun validate(token: String, key: SignatureKey): Boolean {
        try {
            val parser = when (val signatureKey = key.key) {
                is SecretKey -> generateParser(signatureKey)
                is PublicKey -> generateParser(signatureKey)
                else -> return false
            }
            parser.parseSignedClaims(token)
            return true
        } catch (e: JwtException) {
            return false
        }
    }

    fun validateClaimsWith(iss: String, aud: String, token: String, key: SignatureKey): Boolean {
        val claims = getClaims(token, key) ?: return false
        return claims.issuer == iss && claims.audience.contains(aud)
    }

    fun getSubject(token: String, key: SignatureKey): String? {
        val claims = getClaims(token, key) ?: return null
        return claims.subject
    }

    fun parseHeader(token: String): Map<String, String>? {
        try {
            val header = token.split(".")[0]
            val decoder = Base64.getUrlDecoder()
            val decoded = String(decoder.decode(header))
            return objectMapper.readValue(decoded, typeReference)
        } catch (e: DatabindException) {
            return null
        } catch (e: JsonParseException) {
            return null
        } catch (e: IndexOutOfBoundsException) {
            return null
        }
    }

    private fun getClaims(token: String, key: SignatureKey): Claims? {
        val parser = when (val signatureKey = key.key) {
            is SecretKey -> generateParser(signatureKey)
            is PublicKey -> generateParser(signatureKey)
            else -> return null
        }
        return parser.parseSignedClaims(token).payload
    }

    private fun generateParser(key: SecretKey): JwtParser {
        return Jwts.parser()
            .verifyWith(key)
            .build()
    }

    private fun generateParser(key: PublicKey): JwtParser {
        return Jwts.parser()
            .verifyWith(key)
            .build()
    }
}
