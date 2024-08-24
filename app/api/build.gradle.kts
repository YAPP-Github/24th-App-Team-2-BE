import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":app:support:auth"))
    implementation(project(":app:support:exception"))
    implementation(project(":app:websocket"))
    implementation(project(":core"))
    implementation(project(":support:logging"))
    implementation(project(":support:metric"))
    implementation(project(":support:yaml"))

    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-validation:${Versions.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-aop:${Versions.SPRING_BOOT}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.WEBMVC_UI}")
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:${Versions.SENTRY}")
    implementation("io.sentry:sentry-logback:${Versions.SENTRY}")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
