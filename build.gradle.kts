plugins {
    id("io.heapy.kotbot.build")
    application
}

application {
    applicationName = "kotbot"
    mainClass.set("io.heapy.kotbot.Application")
}

dependencies {
    api(tgBotApi) // TODO: Temp
    implementation(logback)

    implementation(micrometer)
    implementation(micrometerPrometheus)

    implementation(ktorClient)
    implementation(ktorClientJackson)

    implementation(config4k)

    implementation(project(":core"))

    implementation(project(":web"))
    implementation(project(":stats"))
    implementation(project(":dao"))
}
