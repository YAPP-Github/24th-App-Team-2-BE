import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    val springBootVersion by properties
    implementation(project(":app:support:auth"))
    implementation(project(":app:websocket"))
    implementation(project(":core"))
    implementation(project(":support:yaml"))

    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
