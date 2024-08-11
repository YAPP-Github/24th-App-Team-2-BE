package com.xorker.draw.version

import com.xorker.draw.exception.NeedForceUpdateException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class ForceUpdateAspect {

    @Around("@annotation(ApiMinVersion)")
    fun checkForceUpdate(joinPoint: ProceedingJoinPoint): Any {
        try {
            val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
            val userAgent = attributes.request.getHeader("User-Agent")

            val (appType, version) = userAgent.getVersionInfo()

            val signature = joinPoint.signature as MethodSignature
            val method = signature.method
            val annotation = method.getAnnotation(ApiMinVersion::class.java)

            if (appType.lowercase() == "android") {
                if (Version.of(annotation.androidVersion) > version) {
                    throw NeedForceUpdateException
                }
            } else if (appType.lowercase() == "ios") {
                if (Version.of(annotation.iosVersion) > version) {
                    throw NeedForceUpdateException
                }
            }
        } catch (e: Exception) {
            throw NeedForceUpdateException
        }

        return joinPoint.proceed()
    }

    private fun String.getVersionInfo(): Pair<String, Version> {
        val lastIndexSpace = if (this.lastIndexOf(' ') == -1) 0 else this.lastIndexOf(' ')
        val lastIndexSlash = this.lastIndexOf('/')

        val appType = this.substring(lastIndexSpace, lastIndexSlash)
        val version = this.substring(lastIndexSlash + 1)

        return Pair(appType, Version.of(version))
    }
}
