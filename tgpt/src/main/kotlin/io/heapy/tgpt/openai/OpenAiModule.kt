package io.heapy.tgpt.openai

import com.openai.client.okhttp.OpenAIOkHttpClient
import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.tgpt.infra.configuration.OpenAiConfiguration

@Module
open class OpenAiModule(
    private val configurationModule: ConfigurationModule,
) {
    open val openAiConfiguration: OpenAiConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = OpenAiConfiguration.serializer(),
                path = "openai",
            )
    }

    open val openAiClient by lazy {
        OpenAIOkHttpClient.builder()
            .apiKey(openAiConfiguration.apiKey)
            .build()
    }

    open val openAiService by lazy {
        OpenAiService(
            client = openAiClient,
            configuration = openAiConfiguration,
        )
    }
}
