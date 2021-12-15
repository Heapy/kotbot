import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
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
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
            "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinCoroutines")
            "implementation"("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialization")

            "testImplementation"(junitApi)
            "testRuntimeOnly"(junitEngine)
            "testImplementation"(mockk)
        }

        repositories {
            mavenCentral()
            maven {
                url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
            }
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            kotlinOptions {
                jvmTarget = kotbotJvmTarget
                languageVersion = "1.5"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-progressive",
                    "-Xopt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
}
