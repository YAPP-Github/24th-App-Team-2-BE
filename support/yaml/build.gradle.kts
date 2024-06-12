import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    val springBootVersion by properties

    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
