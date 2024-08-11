package com.xorker.draw

import com.xorker.draw.support.logging.logger
import com.xorker.draw.version.ApiMinVersion
import org.slf4j.MDC
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    val log = logger()

    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }

    @GetMapping("/test")
    fun test(): String {
        MDC.put("test", "test")
        log.info("Call Test")
        return "pong"
    }

    @ApiMinVersion(androidVersion = "99.99.99", iosVersion = "99.99.99")
    @GetMapping("/force-update")
    fun forceUpdate() {
    }
}
