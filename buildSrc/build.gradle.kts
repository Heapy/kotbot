plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

// This is version for build script
val kotlinPluginVersion = "1.4.21"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinPluginVersion")
}
