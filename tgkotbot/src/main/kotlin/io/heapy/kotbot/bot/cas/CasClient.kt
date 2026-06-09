package io.heapy.kotbot.bot.cas

sealed interface CasResult {
    data object Clean : CasResult

    data class Flagged(
        val offenses: Int?,
        val timeAdded: String?,
        val reasons: List<Int>? = null,
        val messages: List<String>? = null,
    ) : CasResult
}

interface CasClient {
    suspend fun check(userId: Long): CasResult
}
