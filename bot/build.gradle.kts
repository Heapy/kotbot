plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    api(Libs.tgBotApi)
    implementation(Libs.komodoLogging)

    implementation(Libs.micrometer)

    implementation(Libs.ktorClient)
}
