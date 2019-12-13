plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    implementation(Libs.ktorServer)
    implementation(Libs.ktorServerJackson)
}
