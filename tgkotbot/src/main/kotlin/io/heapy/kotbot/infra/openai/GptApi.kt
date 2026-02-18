package io.heapy.kotbot.infra.openai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GptApi(
    private val httpClient: HttpClient,
    private val gptConfig: GptConfig,
) {
    @Serializable
    data class GptConfig(
        val apiKey: String,
        val organization: String,
    )

    suspend fun getModels(): OpenApiList<Model> {
        return httpClient
            .get("https://api.openai.com/v1/models") {
                openAiHeaders()
            }
            .body<OpenApiList<Model>>()
    }

    suspend fun complete(
        chatCompletionRequest: ChatCompletionRequest,
    ): ChatCompletionResponse {
        return httpClient
            .post("https://api.openai.com/v1/chat/completions") {
                openAiHeaders()
                setBody(chatCompletionRequest)
            }
            .body<ChatCompletionResponse>()
    }

    private fun HttpRequestBuilder.openAiHeaders() {
        headers {
            append("Content-Type", "application/json")
            append("Authorization", "Bearer ${gptConfig.apiKey}")
            append("OpenAI-Organization", gptConfig.organization)
        }
    }

    @Serializable
    data class OpenApiList<T>(
        @SerialName("object")
        val obj: String,
        val data: List<T>
    )

    @Serializable
    data class Model(
        val id: String,
        @SerialName("object")
        val obj: String,
        val created: Long,
        @SerialName("owned_by")
        val ownedBy: String,
    )

    @Serializable
    data class ChatCompletionRequest(
        val model: String,
        val messages: List<Message>,
        val temperature: Double,
        @SerialName("max_tokens")
        val maxTokens: Int,
        @SerialName("top_p")
        val topP: Double,
        @SerialName("frequency_penalty")
        val frequencyPenalty: Double,
        @SerialName("presence_penalty")
        val presencePenalty: Double,
        @SerialName("response_format")
        val responseFormat: ResponseFormat,
    ) {
        @Serializable
        data class Message(
            val role: String,
            val content: List<Content>,
        )

        @Serializable
        data class Content(
            val type: String,
            val text: String,
        )

        @Serializable
        data class ResponseFormat(
            val type: String,
        )
    }

    @Serializable
    data class ChatCompletionResponse(
        val id: String,
        @SerialName("object")
        val obj: String,
        val created: Long,
        val model: String,
        val choices: List<Choice>,
        val usage: Usage,
        @SerialName("system_fingerprint")
        val systemFingerprint: String,
    ) {
        @Serializable
        data class Choice(
            val index: Int,
            val message: Message,
            val logprobs: Boolean?,
            @SerialName("finish_reason")
            val finishReason: String,
        )

        @Serializable
        data class Message(
            val role: String,
            val content: String,
            val refusal: String?,
        )

        @Serializable
        data class Usage(
            @SerialName("prompt_tokens")
            val promptTokens: Int,
            @SerialName("completion_tokens")
            val completionTokens: Int,
            @SerialName("total_tokens")
            val totalTokens: Int,
        )
    }
}
