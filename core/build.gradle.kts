import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    api(project(":domain"))
    implementation(project(":adapter:oauth"))
    implementation(project(":adapter:rdb"))

    implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT_VERSION}")
    compileOnly("org.springframework.boot:spring-boot-starter-jdbc:${Versions.SPRING_BOOT_VERSION}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
