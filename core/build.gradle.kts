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
}

tasks.test {
    useJUnitPlatform()
}

kotlin.sourceSets.getByName("main").generatedKotlin.srcDir("src/generated/kotlin")
