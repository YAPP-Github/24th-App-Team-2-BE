package com.xorker.xpr.oidc.apple

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DatabindException
import com.fasterxml.jackson.databind.ObjectMapper
import com.xorker.xpr.exception.UnAuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import java.security.PublicKey
import java.util.*
import org.springframework.stereotype.Component

@Component
internal class AppleIdTokenParser(
    private val objectMapper: ObjectMapper,
) {
    private val typeReference = object : TypeReference<Map<String, String>>() {}

    internal fun parseHeader(token: String): Map<String, String> {
        try {
            val header = token.split(".")[0]
            val decoder = Base64.getUrlDecoder()
            val decoded = String(decoder.decode(header))
            return objectMapper.readValue(decoded, typeReference)
        } catch (e: DatabindException) {
            throw UnAuthenticationException
        } catch (e: IndexOutOfBoundsException) {
            throw UnAuthenticationException
        }
    }

    internal fun getClaims(token: String, publicKey: PublicKey): Claims {
        try {
            val parser = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
            return parser.parseClaimsJws(token).body
        } catch (e: JwtException) {
            throw UnAuthenticationException
        }
    }
}
