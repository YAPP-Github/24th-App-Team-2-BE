import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}")
    implementation("ch.qos.logback.contrib:logback-json-classic:${Versions.LOGBACK}")
    implementation("ch.qos.logback.contrib:logback-jackson:${Versions.LOGBACK}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
