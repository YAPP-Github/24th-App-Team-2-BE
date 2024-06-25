import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":app:support:auth"))
    implementation(project(":core"))
    implementation(project(":support:yaml"))

    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${Versions.SPRING_BOOT}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0") // TODO XPR-55 Merge 후 Versions로 리팩터링 할 예정입니다.
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
