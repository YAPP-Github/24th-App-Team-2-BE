import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("io.jsonwebtoken:jjwt-api:${Versions.JJWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.JJWT_VERSION}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.JJWT_VERSION}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
