import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT_VERSION}")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:${Versions.OPEN_FEIGN_VERSION}")
    implementation("io.jsonwebtoken:jjwt-api:${Versions.JJWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.JJWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.JJWT_VERSION}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
