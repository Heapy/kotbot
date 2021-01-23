plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    implementation(komodo)
    implementation(ktorServer)
    implementation(ktorServerJackson)
}
