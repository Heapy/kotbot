plugins {
    id("io.heapy.kotbot.build")
    application
}

application {
    applicationName = "kotbot"
    mainClassName = "io.heapy.kotbot.Application"
}

dependencies {
    implementation(Libs.logback)
    implementation(Libs.komodoLogging)

    implementation(Libs.komodoDotenv)

    implementation(Libs.micrometer)
    implementation(Libs.micrometerPrometheus)

    implementation(Libs.ktorClient)
    implementation(Libs.ktorClientJackson)

    // fix for https://github.com/ktorio/ktor/issues/1018
    implementation("org.apache.httpcomponents:httpcore-nio:4.4.12")
    implementation("org.apache.httpcomponents:httpcore:4.4.12")

    implementation(project(":web"))
    implementation(project(":bot"))
    implementation(project(":stats"))
    implementation(project(":dao"))
}
