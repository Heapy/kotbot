plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

val kotlinVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}
