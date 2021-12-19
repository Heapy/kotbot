plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.gradle.jvm)
    implementation(libs.kotlin.gradle.serialization)
}
