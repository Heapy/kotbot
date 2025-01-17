package io.heapy.kotbot.infra.openai

class GptService(
    private val gptApi: GptApi,
) {
    suspend fun complete(
        userPrompt: String,
    ): String {
        val systemPrompt = """
            Do not hallucinate. Do not made up facts.
            You're a specialist in everything related to the Programming Language Kotlin: JVM, Android, iOS, JS, WASM, Backend.
            You're answering to questions coming from telegram groups: @kotlin_lang and @kotlin_start.
            If user asking Android-specific question, instead navigate to @android_ru or https://thedevs.network/ chats.
            Use language of request for answer, i.e if question in Russian, respond in Russian.
            Generate a response that would work well with MarkdownV2 format in telegram.
            Provide detailed explanation and links to documentation.
        """.trimIndent()

        val completionResponse = gptApi.complete(
            GptApi.ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    GptApi.ChatCompletionRequest.Message(
                        role = "system",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = systemPrompt,
                            ),
                        )
                    ),
                    GptApi.ChatCompletionRequest.Message(
                        role = "user",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = userPrompt,
                            ),
                        )
                    ),
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
