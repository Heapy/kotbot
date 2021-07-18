plugins {
    id("io.heapy.kotbot.build")
}

kotlin {
    explicitApiWarning()
}

dependencies {
    api(tgBotApi)
    implementation(komodo)
    implementation(komodoLogging)

    implementation(micrometer)

    implementation(ktorClient)
}
