plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

// This is version for build script
val kotlinPluginVersion = "1.5.21"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinPluginVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinPluginVersion")
}
