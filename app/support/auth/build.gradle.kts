import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":support:jwt"))

    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter-security:${Versions.SPRING_BOOT_VERSION}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
