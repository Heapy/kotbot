plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    api(tgBotApi)
    implementation(komodo)
    implementation(komodoLogging)

    implementation(micrometer)

    implementation(ktorClient)
}
