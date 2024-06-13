import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":app:support:auth"))
    implementation(project(":core"))
    implementation(project(":support:yaml"))

    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT_VERSION}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${Versions.SPRING_BOOT_VERSION}")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
