import org.jetbrains.kotlin.config.LanguageVersion

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    explicitApiWarning()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization)

    implementation(libs.ktor.client)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.logback)
}

repositories {
    mavenCentral()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
        languageVersion = LanguageVersion.KOTLIN_2_0.versionString
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-progressive",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}

tasks.test {
    useJUnitPlatform()
}

sourceSets.main.get().java.srcDir("src/generated/kotlin")
