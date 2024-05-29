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
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-progressive",
            "-opt-in=kotlin.RequiresOptIn",
            "-Xcontext-receivers",
        )
    }
}
