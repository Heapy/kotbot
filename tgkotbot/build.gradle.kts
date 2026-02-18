plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

application {
    applicationName = "kotbot"
    mainClass.set("io.heapy.kotbot.Application")
}

tasks.distTar {
    archiveFileName.set("kotbot.tar")
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
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlin.serialization.hocon)

    implementation(libs.logback)

    implementation(libs.micrometer.prometheus)

    implementation(libs.commonmark)

    ksp(libs.komok.tech.di)
    implementation(libs.komok.tech.di.lib)
    implementation(libs.komok.tech.config)
    implementation(libs.komok.tech.logging)
    implementation(libs.komok.tech.config.dotenv)

    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.content.negation)
    implementation(libs.ktor.server)
    implementation(libs.ktor.server.content.negation)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.coroutines.test)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
        )
    }
}

tasks.test {
    useJUnitPlatform {
        excludeTags("ManualTest")
    }
}

val manualTests by tasks.registering(Test::class) {
    useJUnitPlatform {
        includeTags("ManualTest")
    }
}
