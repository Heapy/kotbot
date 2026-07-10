plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.apiParser)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.kotlinpoet)
    implementation(libs.jsoup)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform)
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

val codegenJavaLauncher = javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(25))
    vendor.set(JvmVendorSpec.BELLSOFT)
}

tasks.register<JavaExec>("parseTelegramBotApi") {
    group = "code generation"
    description = "Convert archived Telegram Bot API HTML snapshots to Markdown."
    dependsOn(tasks.named("classes"))
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("Parse")
    javaLauncher.set(codegenJavaLauncher)
    workingDir(rootProject.layout.projectDirectory.asFile)
}

tasks.register<JavaExec>("generateTelegramBotApi") {
    group = "code generation"
    description = "Generate the Telegram Bot API Kotlin sources from the selected HTML snapshot."
    dependsOn(tasks.named("classes"))
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("Generate")
    javaLauncher.set(codegenJavaLauncher)
    workingDir(rootProject.layout.projectDirectory.asFile)
}

tasks.test {
    useJUnitPlatform()
}
