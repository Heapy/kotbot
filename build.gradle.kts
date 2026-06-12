import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

plugins {
    alias(libs.plugins.kotlin.jvm).apply(false)
}

abstract class ManualTestsService : BuildService<BuildServiceParameters.None>

val manualTestsService = gradle.sharedServices.registerIfAbsent(
    "manualTestsService",
    ManualTestsService::class,
) {
    maxParallelUsages.set(1)
}

subprojects {
    tasks.withType<Test>().matching { it.name == "manualTests" }.configureEach {
        usesService(manualTestsService)
    }
}
