package com.xorker.xpr.auth

import com.xorker.xpr.auth.dto.AuthReissueRequest
import com.xorker.xpr.auth.dto.AuthSignInRequest
import com.xorker.xpr.auth.dto.AuthTokenResponse
import com.xorker.xpr.auth.dto.toResponse
import com.xorker.xpr.support.auth.NeedLogin
import com.xorker.xpr.support.auth.PrincipalUser
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authUserCase: AuthUseCase,
) {
    @PostMapping("/api/v1/auth/signin")
    fun signIn(@RequestBody request: AuthSignInRequest): AuthTokenResponse {
        val token = authUserCase.signIn(request.authType, request.token)
        return token.toResponse()
    }

    @PostMapping("/api/v1/auth/reissue")
    fun reissue(@RequestBody request: AuthReissueRequest): AuthTokenResponse {
        val token = authUserCase.reissue(request.refreshToken)
        return token.toResponse()
    }

    @NeedLogin
    @DeleteMapping("/api/v1/auth/withdrawal")
    fun withdrawal(principalUser: PrincipalUser) {
        authUserCase.withdrawal(principalUser.userId)
    }
}