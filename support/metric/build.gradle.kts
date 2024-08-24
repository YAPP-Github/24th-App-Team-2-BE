import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:${Versions.SPRING_BOOT}")
    implementation("io.micrometer:micrometer-registry-prometheus:${Versions.MICROMETER}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
