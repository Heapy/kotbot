package io.heapy.kotbot.infra.openai

import io.heapy.kotbot.ManualTest
import kotlinx.coroutines.test.runTest

class GptServiceTest {
    @ManualTest
    fun `call complete`() = runTest {
        val module = createGptApiModule {

        }

        val response = module.gptService.complete("Kotlin")

        println(response)
    }
}
