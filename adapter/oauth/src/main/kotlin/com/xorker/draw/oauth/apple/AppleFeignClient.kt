package com.xorker.draw.oauth.apple

import com.xorker.draw.oauth.apple.dto.ApplePublicKeys
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "apple-client", url = "https://appleid.apple.com/auth/keys")
internal interface AppleFeignClient {
    @GetMapping
    fun getApplePublicKeys(): ApplePublicKeys
}
