package com.xorker.xpr.auth

import com.xorker.xpr.auth.token.Token
import com.xorker.xpr.user.UserId

interface AuthUseCase {
    fun signIn(authType: AuthType, token: String): Token

    fun reissue(refreshToken: String): Token

    fun withdrawal(userId: UserId)
}