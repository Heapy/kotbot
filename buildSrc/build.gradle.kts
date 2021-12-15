plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

// This is version for build script
val kotlinPluginVersion = "1.6.10-RC"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinPluginVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinPluginVersion")
}
