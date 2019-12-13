package io.heapy.kotbot.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

/**
 * Base configuration for **Kotbot** projects.
 *
 * @author Ruslan Ibragimov
 */
class KotbotPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        plugins.apply("org.jetbrains.kotlin.jvm")

        val kotlinVersion: String by project

        dependencies {
            "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
            "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${Libs.kotlinCoroutines}")

            "testImplementation"(Libs.junitApi)
            "testRuntimeOnly"(Libs.junitEngine)
            "testImplementation"(Libs.mockk)
        }

        repositories {
            jcenter()
            maven { url = uri("https://dl.bintray.com/heapy/heap-dev") }
        }

        tasks.withType<KotlinJvmCompile> {
            kotlinOptions {
                jvmTarget = Libs.jvmTarget
                freeCompilerArgs = freeCompilerArgs + listOf("-progressive")
            }
        }

        tasks.withType<JavaCompile> {
            sourceCompatibility = Libs.jvmTarget
            targetCompatibility = Libs.jvmTarget
        }

        Unit
    }
}
