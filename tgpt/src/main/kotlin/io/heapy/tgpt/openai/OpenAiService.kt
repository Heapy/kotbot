package io.heapy.tgpt.openai

import com.openai.client.OpenAIClient
import com.openai.core.MultipartField
import com.openai.models.audio.transcriptions.TranscriptionCreateParams
import com.openai.models.responses.Response
import com.openai.models.responses.ResponseCreateParams
import java.io.InputStream
import io.heapy.komok.tech.logging.Logger
import io.heapy.tgpt.infra.Loom
import io.heapy.tgpt.infra.configuration.OpenAiConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenAiService(
    private val client: OpenAIClient,
    private val configuration: OpenAiConfiguration,
) {
    val systemPrompt: String get() = configuration.systemPrompt
    val webSearchEnabled: Boolean get() = configuration.webSearchEnabled

    suspend fun createResponse(
        params: ResponseCreateParams,
    ): Response {
        return withContext(Dispatchers.Loom) {
            log.info("Calling responses API with model ${configuration.model}")
            client.responses().create(params)
        }
    }

    suspend fun transcribe(
        audioBytes: ByteArray,
    ): String {
        return withContext(Dispatchers.Loom) {
            log.info("Calling Whisper transcription")
            val params = TranscriptionCreateParams.builder()
                .model(configuration.whisperModel)
                .file(
                    MultipartField.builder<InputStream>()
                        .value(audioBytes.inputStream())
                        .filename("audio.ogg")
                        .build()
                )
                .build()
            client.audio().transcriptions().create(params).asTranscription().text()
        }
    }

    fun model(): String = configuration.model

    fun maxTokens(): Int = configuration.maxTokens

    private companion object : Logger()
}
