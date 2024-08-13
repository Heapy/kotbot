package io.heapy.kotbot.infra.openai

class GptService(
    private val gptApi: GptApi,
) {
    suspend fun complete(
        prompt: String,
    ): String {
        val completionResponse = gptApi.complete(
            GptApi.ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    GptApi.ChatCompletionRequest.Message(
                        role = "system",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = """
                                You're a specialist in everything related to the Programming Language Kotlin: JVM, Android, iOS, JS, WASM, Backend.
                                You're answering to questions coming from telegram groups: @kotlin_lang and @kotlin_start
                                Use language of request for answer
                            """.trimIndent()
                            ),
                        )
                    ),
                    GptApi.ChatCompletionRequest.Message(
                        role = "user",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = prompt,
                            ),
                        )
                    )
                ),
                temperature = 1.01,
                maxTokens = 1024,
                topP = 1.0,
                frequencyPenalty = 0.0,
                presencePenalty = 0.0,
                responseFormat = GptApi.ChatCompletionRequest.ResponseFormat(
                    type = "text"
                )
            )
        )

        return completionResponse
            .choices
            .first()
            .message
            .content
    }
}
