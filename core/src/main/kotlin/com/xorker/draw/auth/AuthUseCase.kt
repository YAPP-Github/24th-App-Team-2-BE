package com.xorker.draw.auth

import com.xorker.draw.auth.token.Token
import com.xorker.draw.user.UserId

interface AuthUseCase {
    fun signIn(authType: AuthType, token: String): Token

    fun reissue(refreshToken: String): Token

    fun withdrawal(userId: UserId)
}
