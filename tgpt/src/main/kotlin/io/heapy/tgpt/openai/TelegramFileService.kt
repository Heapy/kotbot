package io.heapy.tgpt.openai

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.method.GetFile
import io.heapy.tgpt.infra.Loom
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TelegramFileService(
    private val kotbot: Kotbot,
    private val httpClient: HttpClient,
    private val botToken: String,
) {
    suspend fun downloadFile(fileId: String): ByteArray {
        return withContext(Dispatchers.Loom) {
            val fileResponse = kotbot.execute(GetFile(file_id = fileId))
            val filePath = fileResponse.file_path
                ?: error("File path is null for file_id=$fileId")
            val url = "https://api.telegram.org/file/bot$botToken/$filePath"
            log.info("Downloading file from Telegram: $filePath")
            httpClient.get(url).readRawBytes()
        }
    }

    private companion object : Logger()
}
