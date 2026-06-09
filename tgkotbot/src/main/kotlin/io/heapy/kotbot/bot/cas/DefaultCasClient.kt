package io.heapy.kotbot.bot.cas

import io.heapy.komok.tech.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DefaultCasClient(
    private val client: HttpClient,
) : CasClient {
    override suspend fun check(userId: Long): CasResult {
        return try {
            val response = client
                .get("https://api.cas.chat/check?user_id=$userId") {
                    timeout {
                        connectTimeoutMillis = CONNECT_TIMEOUT_MS
                        requestTimeoutMillis = REQUEST_TIMEOUT_MS
                        socketTimeoutMillis = SOCKET_TIMEOUT_MS
                    }
                }
                .body<CasResponse>()

            if (response.ok) {
                CasResult.Flagged(
                    offenses = response.result?.offenses,
                    timeAdded = response.result?.timeAdded,
                    reasons = response.result?.reasons,
                    messages = response.result?.messages,
                )
            } else {
                CasResult.Clean
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // CAS unavailable/timeout -> fail open (treat as clean), never stall the join.
            log.warn("CAS check failed for user {}, failing open (treating as clean)", userId, e)
            CasResult.Clean
        }
    }

    @Serializable
    private data class CasResponse(
        val ok: Boolean,
        val result: CasResultDetail? = null,
    )

    @Serializable
    private data class CasResultDetail(
        val offenses: Int? = null,
        @SerialName("time_added")
        val timeAdded: String? = null,
        val reasons: List<Int>? = null,
        val messages: List<String>? = null,
    )

    private companion object : Logger() {
        private const val CONNECT_TIMEOUT_MS = 3_000L
        private const val REQUEST_TIMEOUT_MS = 5_000L
        private const val SOCKET_TIMEOUT_MS = 5_000L
    }
}
