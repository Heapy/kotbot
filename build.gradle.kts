plugins {
    id("io.heapy.kotbot.build")
    application
}

application {
    applicationName = "kotbot"
    mainClass.set("io.heapy.kotbot.Application")
}

dependencies {
    // TODO: Remove
    compileOnly("org.telegram:telegrambots:5.5.0")

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.mockk)

    implementation(libs.logback)

    implementation(libs.micrometer.prometheus)

    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.content.negation)
    implementation(libs.ktor.server)
    implementation(libs.ktor.server.content.negation)

    implementation(libs.config4k)

    implementation(project(":core"))
}
