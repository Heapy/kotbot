package io.heapy.kotbot.infra.openai

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.HttpClientModule

@Module
open class GptApiModule(
    private val httpClientModule: HttpClientModule,
    private val configurationModule: ConfigurationModule,
) {
    open val gptConfig: GptApi.GptConfig by lazy {
        configurationModule.config
            .read(
                deserializer = GptApi.GptConfig.serializer(),
                path = "gpt",
            )
    }

    open val gptApi by lazy {
        GptApi(
            httpClient = httpClientModule.httpClient,
            gptConfig = gptConfig,
        )
    }

    open val gptService by lazy {
        GptService(
            gptApi = gptApi,
        )
    }
}
