package io.heapy.kotbot.bot.dao

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class UserContext(
    val id: Long,
    val created: Long,
    val statuses: List<Status> = listOf(),
)

@Serializable
data class Status(
    val name: String,
    val expires: Long,
)

class UserContextDao(
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun get(
        id: Long,
    ): UserContext? = withContext(ioDispatcher) {
        null
    }

    suspend fun put(
        userContext: UserContext,
    ) = withContext(ioDispatcher) {
    }
}
