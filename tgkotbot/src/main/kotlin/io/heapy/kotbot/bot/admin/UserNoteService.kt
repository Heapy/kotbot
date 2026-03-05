package io.heapy.kotbot.bot.admin

import io.heapy.kotbot.bot.dao.UpdateDao
import io.heapy.kotbot.bot.dao.UserContext
import io.heapy.kotbot.bot.dao.UserContextDao
import io.heapy.kotbot.infra.jdbc.TransactionProvider
import io.heapy.kotbot.infra.openai.GptApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class UserNoteService(
    private val gptApi: GptApi,
    private val updateDao: UpdateDao,
    private val userContextDao: UserContextDao,
    private val transactionProvider: TransactionProvider,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    context(userContext: UserContext)
    fun startGeneration() {
        scope.launch {
            try {
                val note = generate(userContext)
                saveNote(userContext.internalId, note)
            } catch (e: Exception) {
                log.error("Note generation failed for user ${userContext.internalId}", e)
            }
        }
    }

    private suspend fun generate(userContext: UserContext): String {
        val messages = transactionProvider.transaction {
            updateDao.getUserMessageTexts(userContext.telegramId)
        }

        if (messages.isEmpty()) return "No messages found for this user."

        val batches = messages.chunked(BATCH_SIZE)
        val batchSummaries = batches.map { batch ->
            withRetry { extractBatchFacts(batch) }
        }

        return if (batchSummaries.size == 1) {
            batchSummaries.first()
        } else {
            withRetry { combineSummaries(batchSummaries) }
        }
    }

    private suspend fun saveNote(internalId: Long, note: String) {
        transactionProvider.transaction {
            val fresh = userContextDao.getByInternalId(internalId)
            if (fresh != null) userContextDao.updateNote(fresh, note)
        }
    }

    private suspend fun <T> withRetry(maxAttempts: Int = 3, block: suspend () -> T): T {
        repeat(maxAttempts - 1) { attempt ->
            runCatching { return block() }
                .onFailure { e ->
                    log.warn("Attempt ${attempt + 1} failed, retrying in ${attempt + 1}s", e)
                    delay(1000L * (attempt + 1))
                }
        }
        return block()
    }

    private suspend fun extractBatchFacts(messages: List<String>): String {
        val messagesText = messages.joinToString("\n---\n")
        return gptApi.complete(
            GptApi.ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    GptApi.ChatCompletionRequest.Message(
                        role = "system",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = """
                                    Analyze Telegram messages to extract facts about the user.
                                    Focus on: area of expertise, programming topics they work with,
                                    kind of questions they ask, experience level.
                                    Be concise and factual. Output plain text, no markdown, 3-5 sentences.
                                """.trimIndent(),
                            ),
                        ),
                    ),
                    GptApi.ChatCompletionRequest.Message(
                        role = "user",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = "Extract facts about this user from their messages:\n\n$messagesText",
                            ),
                        ),
                    ),
                ),
                temperature = 0.3,
                maxTokens = 512,
                topP = 1.0,
                frequencyPenalty = 0.0,
                presencePenalty = 0.0,
                responseFormat = GptApi.ChatCompletionRequest.ResponseFormat(type = "text"),
            ),
        ).choices.first().message.content
    }

    private suspend fun combineSummaries(summaries: List<String>): String {
        val summariesText = summaries.joinToString("\n\n---\n\n")
        return gptApi.complete(
            GptApi.ChatCompletionRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    GptApi.ChatCompletionRequest.Message(
                        role = "system",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = """
                                    Combine multiple observations about a Telegram user into a single concise profile.
                                    Focus on: area of expertise, programming topics, kind of questions asked, experience level.
                                    Output plain text, no markdown, 3-5 sentences.
                                """.trimIndent(),
                            ),
                        ),
                    ),
                    GptApi.ChatCompletionRequest.Message(
                        role = "user",
                        content = listOf(
                            GptApi.ChatCompletionRequest.Content(
                                type = "text",
                                text = "Combine these observations into a user profile:\n\n$summariesText",
                            ),
                        ),
                    ),
                ),
                temperature = 0.3,
                maxTokens = 512,
                topP = 1.0,
                frequencyPenalty = 0.0,
                presencePenalty = 0.0,
                responseFormat = GptApi.ChatCompletionRequest.ResponseFormat(type = "text"),
            ),
        ).choices.first().message.content
    }

    private companion object {
        private val log = LoggerFactory.getLogger(UserNoteService::class.java)
        private const val BATCH_SIZE = 30
    }
}