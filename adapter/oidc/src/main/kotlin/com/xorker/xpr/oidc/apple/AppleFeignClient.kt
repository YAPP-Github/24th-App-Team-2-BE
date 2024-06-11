package com.xorker.xpr.oidc.apple

import com.xorker.xpr.oidc.apple.dto.ApplePublicKeys
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "apple-feign-client", url = "https://appleid.apple.com/auth/keys")
internal interface AppleFeignClient {
    @GetMapping
    fun getApplePublicKeys(): ApplePublicKeys
}
