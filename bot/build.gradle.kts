plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    api(tgBotApi)
    implementation(komodoLogging)

    implementation(micrometer)

    implementation(ktorClient)
}
