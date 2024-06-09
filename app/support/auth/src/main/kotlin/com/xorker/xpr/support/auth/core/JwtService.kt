package com.xorker.xpr.support.auth.core

import com.xorker.xpr.user.UserId
import org.springframework.stereotype.Service

@Service
internal class JwtService : TokenUseCase {
    override fun getUserId(token: String): UserId {
        // TODO
        throw JwtValidationFailException
    }
}
