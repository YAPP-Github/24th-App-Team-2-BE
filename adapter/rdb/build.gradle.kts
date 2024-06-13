import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

dependencies {
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:${Versions.SPRING_BOOT_VERSION}")
    runtimeOnly("mysql:mysql-connector-java:${Versions.MYSQL_VERSION}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.flywaydb:flyway-core:${Versions.FLYWAY_VERSION}")
    implementation("org.flywaydb:flyway-mysql:${Versions.FLYWAY_VERSION}")
//    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
//    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
