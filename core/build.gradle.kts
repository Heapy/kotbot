plugins {
    id("io.heapy.kotbot.build")
}

kotlin {
    explicitApiWarning()
}

dependencies {
    implementation(ktorClient)
}
