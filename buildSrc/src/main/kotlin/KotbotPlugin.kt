import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

class KotbotPlugin : Plugin<Project> {
    override fun apply(project: Project) = with(project) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        repositories {
            mavenCentral()
            maven {
                url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
            }
        }

        tasks.withType<KotlinJvmCompile>().configureEach {
            kotlinOptions {
                jvmTarget = "11"
                languageVersion = "1.6"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-progressive",
                    "-Xopt-in=kotlin.RequiresOptIn"
                )
            }
        }
    }
}
