plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

application {
    applicationName = "kotbot"
    mainClass.set("io.heapy.kotbot.Application")
}

tasks.distZip {
    enabled = false
}

dependencies {
    implementation(projects.core)
    implementation(projects.tgkotbotDatabase)
    implementation(projects.tgkotbotDataops)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.serialization.hocon)

    implementation(libs.logback)

    implementation(libs.micrometer.prometheus)

    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.content.negation)
    implementation(libs.ktor.server)
    implementation(libs.ktor.server.content.negation)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-progressive",
            "-opt-in=kotlin.RequiresOptIn",
            "-Xcontext-receivers",
        )
    }
}

tasks.test {
    useJUnitPlatform()
}
