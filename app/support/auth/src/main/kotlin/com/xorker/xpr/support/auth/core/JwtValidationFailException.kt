package com.xorker.xpr.support.auth.core

import org.springframework.security.core.AuthenticationException

internal data object JwtValidationFailException : AuthenticationException("Jwt validation 실패") {
    private fun readResolve(): Any = JwtValidationFailException
}
