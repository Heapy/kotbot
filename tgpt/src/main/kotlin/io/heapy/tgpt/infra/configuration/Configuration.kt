package io.heapy.tgpt.infra.configuration

import kotlinx.serialization.Serializable

@Serializable
data class BotConfiguration(
    val token: String,
)

@Serializable
data class OpenAiConfiguration(
    val apiKey: String,
    val model: String = "gpt-5.2",
    val whisperModel: String = "whisper-1",
    val systemPrompt: String = "You are a helpful assistant.",
    val maxTokens: Int = 4096,
)
