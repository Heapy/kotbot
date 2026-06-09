package io.heapy.kotbot.infra.openai

import com.openai.client.okhttp.OpenAIOkHttpClient
import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module

@Module
class GptApiModule(
    private val configurationModule: ConfigurationModule,
) {
    val gptConfig: GptApi.GptConfig by lazy {
        configurationModule.config
            .read(
                deserializer = GptApi.GptConfig.serializer(),
                path = "gpt",
            )
    }

    val openAiClient by lazy {
        OpenAIOkHttpClient.builder()
            .apiKey(gptConfig.apiKey)
            .organization(gptConfig.organization)
            .build()
    }

    val gptApi by lazy {
        GptApi(
            client = openAiClient,
        )
    }

    val gptService by lazy {
        GptService(
            gptApi = gptApi,
        )
    }
}
