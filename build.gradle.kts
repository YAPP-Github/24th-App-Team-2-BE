plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    dependencies {
        val springBootVersion by properties

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")

        // test
        testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(17)
}
