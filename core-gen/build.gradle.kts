import org.jetbrains.kotlin.config.LanguageVersion

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlinpoet)
    implementation(libs.jsoup)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
        languageVersion = LanguageVersion.KOTLIN_2_0.versionString
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-progressive",
            "-opt-in=kotlin.RequiresOptIn",
            "-Xcontext-receivers",
        )
    }
}
