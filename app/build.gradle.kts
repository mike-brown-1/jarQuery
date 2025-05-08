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
    alias(libs.plugins.gitversion)
    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.clikt)

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
    mainClass = "jarquery.AppKt"
}

version = "1.2.0"
val now = ZonedDateTime.now()
val dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME

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
            "Implementation-Vendor" to System.getProperty("user.name"),
            "Built-On" to dtf.format(now),
            "Created-By" to "Gradle ${gradle.gradleVersion}",
            "Build-Jdk" to "${System.getProperty("java.version")} (${System.getProperty("java.vendor")} ${System.getProperty("java.vm.version")})",
            "Build-OS" to "${System.getProperty("os.name")} ${System.getProperty("os.arch")} ${System.getProperty("os.version")}"
        )
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
