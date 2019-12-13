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

    implementation(project(":web"))
    implementation(project(":bot"))
    implementation(project(":stats"))
    implementation(project(":dao"))
}
