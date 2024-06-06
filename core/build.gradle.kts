import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    val springBootVersion by properties
    api(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
