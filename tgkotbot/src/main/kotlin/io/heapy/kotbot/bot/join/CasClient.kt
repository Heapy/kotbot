package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.infra.configuration.CasConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Result of a CAS (Combot Anti-Spam) lookup for a single user.
 *
 * [Clean] also covers the fail-open case: a CAS timeout/IO failure is treated as clean so the
 * join flow is never stalled by a slow or unavailable CAS API.
 */
sealed interface CasResult {
    data object Clean : CasResult

    /**
     * [messages] holds the actual spam text CAS banned the user for, and [reasons] the CAS reason
     * codes. Both are surfaced to admins on appeal so a write-access decision can be made on evidence.
     */
    data class Flagged(
        val offenses: Int?,
        val timeAdded: String?,
        val reasons: List<Int>? = null,
        val messages: List<String>? = null,
    ) : CasResult
}

/**
 * Checks users against `api.cas.chat` with a CAS-specific request timeout.
 *
 * Moved off the per-message rule path (it used to run inside the open polling transaction with no
 * timeout); it is now only called once per join, after a passed challenge, for non-verified users.
 */
class CasClient(
    private val client: HttpClient,
    private val casConfiguration: CasConfiguration,
) {
    suspend fun check(userId: Long): CasResult {
        // Test/manual override: treat configured users as CAS-flagged without calling the API.
        if (userId == casConfiguration.forceFlagged) {
            log.info("User {} force-flagged via cas.forceFlagged config, skipping CAS API", userId)
            return CasResult.Flagged(
                offenses = null,
                timeAdded = null,
                reasons = null,
                messages = listOf(FORCED_FLAG_MESSAGE),
            )
        }

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
        private const val FORCED_FLAG_MESSAGE = "Forced CAS flag via configuration (cas.forceFlagged)"
    }
}
