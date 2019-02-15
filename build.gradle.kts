import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    application
    kotlin("jvm").version(Libs.kotlinVersion)
}

allprojects {
    apply<KotlinPluginWrapper>()

    repositories {
        jcenter()
        maven { 
            url = uri("https://dl.bintray.com/heapy/heap-dev") 
        }
    }

    tasks.withType<KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += listOf("-progressive")
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    dependencies {
        compile(Libs.kotlinStdlib)
        compile(Libs.kotlinCoroutines)
        compile(Libs.logback)
        compile(Libs.komodoLogging)

        compile(Libs.micrometer)
        compile(Libs.micrometerPrometheus)

        testCompile(Libs.junitApi)
        testRuntimeOnly(Libs.junitEngine)
        testCompile(Libs.mockk)
    }
}

dependencies {
    compile(project(":web"))
    compile(project(":bot"))
    compile(project(":stats"))
    compile(project(":dao"))
}

application {
    applicationName = "kotbot"
    mainClassName = "io.heapy.kotbot.Application"
}
