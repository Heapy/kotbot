plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jooq.codegen)
    implementation(libs.flyway)
    implementation(libs.postgresql)
    implementation(libs.logback)
}
