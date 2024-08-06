import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":app:support:auth"))
    implementation(project(":app:support:exception"))
    implementation(project(":core"))
    implementation(project(":support:yaml"))
    implementation(project(":support:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-websocket:${Versions.SPRING_BOOT}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
