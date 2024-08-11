package com.xorker.draw

import com.xorker.draw.support.logging.logger
import com.xorker.draw.version.ApiMinVersion
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.MDC
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
@Tag(name = "개발용 API")
@RestController
class HealthCheckController {
    val log = logger()

    @Operation(hidden = true)
    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }

    @Operation(hidden = true)
    @GetMapping("/test")
    fun test(): String {
        MDC.put("test", "test")
        log.info("Call Test")
        return "pong"
    }

    @Operation(summary = "강업 테스트")
    @ApiMinVersion(androidVersion = "99.99.99", iosVersion = "99.99.99")
    @GetMapping("/force-update")
    fun forceUpdate() {}
}
