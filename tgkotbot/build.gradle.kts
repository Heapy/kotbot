@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

application {
    applicationName = "kotbot"
    mainClass.set("io.heapy.kotbot.Application")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)

    implementation(libs.logback)

    implementation(libs.micrometer.prometheus)

    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.content.negation)
    implementation(libs.ktor.server)
    implementation(libs.ktor.server.content.negation)

    implementation(libs.config4k)

    implementation(libs.mapdb)

    implementation(projects.core)
}

repositories {
    mavenCentral()
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.7"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-progressive",
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}
