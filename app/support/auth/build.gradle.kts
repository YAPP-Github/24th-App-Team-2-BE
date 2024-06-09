import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    val springBootVersion by properties
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
