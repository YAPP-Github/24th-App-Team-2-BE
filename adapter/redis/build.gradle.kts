import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":support:metric"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis:${Versions.SPRING_BOOT}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
