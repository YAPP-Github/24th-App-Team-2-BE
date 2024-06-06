package com.xorker.xpr.support.auth.core

import com.xorker.xpr.user.UserId

internal interface TokenUseCase {
    fun getUserId(token: String): UserId
}
