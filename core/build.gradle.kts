import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
}

dependencies {
    api(project(":domain"))
    api(project(":event"))
    implementation(project(":adapter:memory"))
    implementation(project(":adapter:oauth"))
    implementation(project(":adapter:rdb"))
    implementation(project(":adapter:redis"))
    implementation(project(":adapter:webhook:discord"))
    implementation(project(":support:jwt"))
    implementation(project(":support:time"))

    implementation("org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}")
    compileOnly("org.springframework.boot:spring-boot-starter-jdbc:${Versions.SPRING_BOOT}")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
