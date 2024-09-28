import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":event"))
    implementation(project(":support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-webflux:${Versions.SPRING_BOOT}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
