import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
