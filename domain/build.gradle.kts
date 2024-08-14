import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
