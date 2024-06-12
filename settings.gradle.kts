pluginManagement {
    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    plugins {
        id("org.springframework.boot") version springBootVersion
        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion
    }
}

include(
    "adapter:rdb",
    "app:api",
    "app:support:auth",
    "domain",
    "core",
    "support:yaml",
)
