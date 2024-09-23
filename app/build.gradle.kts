import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

//
// See gradle/libs.versions.toml for specific version numbers
//

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.versions)
    alias(libs.plugins.detekt)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.picocli)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "jarquery.JarQueryKt"
}

version = "0.9.1"
val now = ZonedDateTime.now()
val dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm z")

tasks.register("updateVersionInSource") {
    group = "build"
    description = "Updates the appVersion in Config.kt"

    doLast {
        val versionFile = file("src/main/kotlin/jarquery/Config.kt")
        val content = versionFile.readText()
        val updatedContent = content.replace(
            """const val APPVERSION = ".*"""".toRegex(),
            """const val APPVERSION = "${project.version}""""
        )
        versionFile.writeText(updatedContent)
    }
}

tasks.named("compileKotlin") {
    dependsOn("updateVersionInSource")
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to "jarquery",
            "Implementation-Version" to archiveVersion,
            "Implementation-Vendor" to "Mike Brown",
            "Built-On" to dtf.format(now)
        )
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
