package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

interface TokenUseCase {
    fun getUserId(token: String): UserId?
}
