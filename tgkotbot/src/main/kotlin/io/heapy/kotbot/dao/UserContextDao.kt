package io.heapy.kotbot.dao

import kotlinx.coroutines.Dispatchers
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

class UserContextDao {
    suspend fun get(
        id: Long,
    ): UserContext? = withContext(Dispatchers.IO) {
        null
    }

    suspend fun put(
        userContext: UserContext,
    ) = withContext(Dispatchers.IO) {
    }
}
