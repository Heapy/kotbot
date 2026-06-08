package io.heapy.kotbot.infra.openai

import com.openai.client.okhttp.OpenAIOkHttpClient
import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module

@Module
open class GptApiModule(
    private val configurationModule: ConfigurationModule,
) {
    open val gptConfig: GptApi.GptConfig by lazy {
        configurationModule.config
            .read(
                deserializer = GptApi.GptConfig.serializer(),
                path = "gpt",
            )
    }

    open val openAiClient by lazy {
        OpenAIOkHttpClient.builder()
            .apiKey(gptConfig.apiKey)
            .organization(gptConfig.organization)
            .build()
    }

    open val gptApi by lazy {
        GptApi(
            client = openAiClient,
        )
    }

    open val gptService by lazy {
        GptService(
            gptApi = gptApi,
        )
    }
}
