import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.51.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("info.picocli:picocli:4.7.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "jarQuery.AppKt"
}

version = "0.2.0"
val now = ZonedDateTime.now()
val dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm z")

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to "jarQuery",
            "Implementation-Version" to archiveVersion,
            "Built-On" to dtf.format(now)
        )
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
