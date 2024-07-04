package com.xorker.draw.support.auth.core

import com.xorker.draw.user.UserId

internal interface TokenUseCase {
    fun getUserId(token: String): UserId?
}
