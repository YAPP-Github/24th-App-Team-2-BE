package com.xorker.draw.auth

import com.xorker.draw.auth.dto.AuthReissueRequest
import com.xorker.draw.auth.dto.AuthSignInRequest
import com.xorker.draw.auth.dto.AuthTokenResponse
import com.xorker.draw.auth.dto.toResponse
import com.xorker.draw.support.auth.NeedLogin
import com.xorker.draw.support.auth.PrincipalUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth APIs")
@RestController
class AuthController(
    private val authUserCase: AuthUseCase,
) {
    @Operation(summary = "소셜 로그인 API")
    @PostMapping("/api/v1/auth/signin")
    fun signIn(@RequestBody request: AuthSignInRequest): AuthTokenResponse {
        val token = authUserCase.signIn(request.authType, request.token)
        return token.toResponse()
    }

    @Operation(summary = "익명 로그인 API")
    @PostMapping("/api/v1/auth/anonymous-signin")
    fun anonymousSignin(): AuthTokenResponse {
        val token = authUserCase.anonymousSignIn()
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
