import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

apply<KotlinPlatformJvmPlugin>()

dependencies {
    implementation(Libs.ktorServer)
    implementation(Libs.ktorJackson)
}
