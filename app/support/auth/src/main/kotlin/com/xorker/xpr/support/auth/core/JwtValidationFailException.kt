package com.xorker.xpr.support.auth.core

import org.springframework.security.core.AuthenticationException

internal object JwtValidationFailException : AuthenticationException("Jwt validation 실패")