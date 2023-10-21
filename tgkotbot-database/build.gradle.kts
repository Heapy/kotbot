plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jooq.core)
    api(libs.hikari)
    api(libs.postgresql)
}
