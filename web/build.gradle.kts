plugins {
    id("io.heapy.kotbot.build")
}

dependencies {
    implementation(ktorServer)
    implementation(ktorServerJackson)
}
