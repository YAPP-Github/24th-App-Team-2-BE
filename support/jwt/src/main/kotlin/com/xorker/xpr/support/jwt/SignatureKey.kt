package com.xorker.xpr.support.jwt

import java.security.Key

interface SignatureKey {
    val key: Key
}
