plugins {
    `kotbot-publish-conventions`
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
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin.sourceSets.getByName("main").generatedKotlin.srcDir("src/generated/kotlin")
