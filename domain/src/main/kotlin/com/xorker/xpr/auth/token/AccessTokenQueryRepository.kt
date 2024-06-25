package com.xorker.xpr.auth.token

import com.xorker.xpr.user.UserId

interface AccessTokenQueryRepository {
    fun getUserIdOrThrow(accessToken: String): UserId
}
