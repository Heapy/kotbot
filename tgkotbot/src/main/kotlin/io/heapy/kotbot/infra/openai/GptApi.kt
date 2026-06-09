package io.heapy.kotbot.infra.openai

import com.openai.client.OpenAIClient
import com.openai.models.chat.completions.ChatCompletionCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

class GptApi(
    private val client: OpenAIClient,
) {
    @Serializable
    data class GptConfig(
        val apiKey: String,
        val organization: String,
    )

    suspend fun complete(
        model: String,
        systemPrompt: String,
        userPrompt: String,
        temperature: Double,
        maxTokens: Int,
        topP: Double = 1.0,
        frequencyPenalty: Double = 0.0,
        presencePenalty: Double = 0.0,
    ): String = withContext(Dispatchers.IO) {
        val params = ChatCompletionCreateParams.builder()
            .model(model)
            .addSystemMessage(systemPrompt)
            .addUserMessage(userPrompt)
            .temperature(temperature)
            .maxCompletionTokens(maxTokens.toLong())
            .topP(topP)
            .frequencyPenalty(frequencyPenalty)
            .presencePenalty(presencePenalty)
            .build()

        client.chat()
            .completions()
            .create(params)
            .choices()
            .first()
            .message()
            .content()
            .orElse("")
    }
}
