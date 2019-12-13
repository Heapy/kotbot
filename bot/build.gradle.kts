plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    implementation(Libs.tgBotApi)
    implementation(Libs.komodoLogging)

    implementation(Libs.ktorClient)
}
