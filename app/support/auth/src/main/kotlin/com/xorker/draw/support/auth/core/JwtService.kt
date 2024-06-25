package com.xorker.draw.support.auth.core

import com.xorker.draw.user.UserId
import org.springframework.stereotype.Service

@Service
internal class JwtService : TokenUseCase {
    override fun getUserId(token: String): UserId {
        // TODO
        throw JwtValidationFailException
    }
}
