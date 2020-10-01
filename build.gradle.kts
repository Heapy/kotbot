plugins {
    id("io.heapy.kotbot.build")
    application
}

application {
    applicationName = "kotbot"
    mainClassName = "io.heapy.kotbot.Application"
}

dependencies {
    implementation(logback)
    implementation(komodoLogging)

    implementation(komodoDotenv)

    implementation(micrometer)
    implementation(micrometerPrometheus)

    implementation(ktorClient)
    implementation(ktorClientJackson)

    implementation(config4k)

    implementation(project(":web"))
    implementation(project(":bot"))
    implementation(project(":stats"))
    implementation(project(":dao"))
}
