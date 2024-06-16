import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("io.jsonwebtoken:jjwt-api:${Versions.JJWT}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Versions.JJWT}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Versions.JJWT}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
