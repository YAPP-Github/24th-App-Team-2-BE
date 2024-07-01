package com.xorker.draw.auth.token

import com.xorker.draw.user.UserId

interface AccessTokenQueryRepository {
    fun getUserIdOrThrow(accessToken: String): UserId
}
