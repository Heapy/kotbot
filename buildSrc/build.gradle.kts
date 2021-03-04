plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

// This is version for build script
val kotlinPluginVersion = "1.4.31"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinPluginVersion")
}
