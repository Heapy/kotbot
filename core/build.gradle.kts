plugins {
    `kotbot-publish-conventions`
    `java-test-fixtures`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    explicitApiWarning()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.serialization.json)

    implementation(libs.ktor.client)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform)
    testImplementation(libs.mockk)
    testImplementation(libs.logback)
    testImplementation(libs.komok.tech.config.dotenv)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.ktor.client.mock)

    testFixturesImplementation(libs.junit)
    testFixturesImplementation(libs.komok.tech.config.dotenv)
    testFixturesImplementation(libs.logback)
    testFixturesImplementation(libs.kotlin.coroutines)
    testFixturesImplementation(libs.ktor.client)
    testFixturesImplementation(libs.kotlin.serialization.json)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }
    compilerOptions {
        allWarningsAsErrors = true
        freeCompilerArgs.addAll(
            "-Xreturn-value-checker=full",
            "-Xname-based-destructuring=complete",
        )
    }
}

tasks.test {
    useJUnitPlatform {
        excludeTags("ManualTest")
    }
}

val manualTests by tasks.registering(Test::class) {
    useJUnitPlatform {
        includeTags("ManualTest")
    }
}

kotlin.sourceSets.getByName("main").generatedKotlin.srcDir("src/generated/kotlin")
