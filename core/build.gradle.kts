@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
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
        jvmTarget = "11"
        languageVersion = "1.7"
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
