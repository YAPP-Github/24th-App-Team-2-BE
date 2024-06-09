package com.xorker.xpr.support.auth.security

import com.xorker.xpr.exception.UnAuthenticationException
import com.xorker.xpr.exception.UnAuthorizedException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
internal class SecurityExceptionHandler(
    @Qualifier("handlerExceptionResolver") private val handler: HandlerExceptionResolver,
) : AuthenticationEntryPoint, AccessDeniedHandler {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authenticationException: AuthenticationException,
    ) {
        handler.resolveException(request, response, null, UnAuthenticationException)
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        handler.resolveException(request, response, null, UnAuthorizedException)
    }
}
