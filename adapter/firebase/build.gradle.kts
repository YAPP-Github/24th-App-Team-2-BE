import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":event"))
    implementation(project(":app:support:i18n"))
    implementation(project(":support:metric"))

    implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}")
    implementation("com.google.firebase:firebase-admin:${Versions.FIREBASE}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
