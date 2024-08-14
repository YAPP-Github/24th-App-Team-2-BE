package com.xorker.draw.version

@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class ApiMinVersion(
    val androidVersion: String = "0.0.0",
    val iosVersion: String = "0.0.0",
)
