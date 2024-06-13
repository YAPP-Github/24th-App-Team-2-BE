plugins {
    id("org.springframework.boot") version Versions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version Versions.SPRING_DEPENDENCY_MANAGEMENT_VERSION
    kotlin("jvm") version Versions.KOTLIN_VERSION
    kotlin("kapt") version Versions.KOTLIN_VERSION
    kotlin("plugin.spring") version Versions.KOTLIN_VERSION
    kotlin("plugin.jpa") version Versions.KOTLIN_VERSION
    kotlin("plugin.allopen") version Versions.KOTLIN_VERSION
    kotlin("plugin.noarg") version Versions.KOTLIN_VERSION
    id("org.jlleitschuh.gradle.ktlint") version Versions.KTLINT_VERSION
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON_VERSION}")
        implementation("org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT_VERSION}")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(Versions.JDK_VERSION)
}
