package io.heapy.kotbot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.mapdb.DB
import org.mapdb.HTreeMap
import org.mapdb.Serializer

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

class UserContextStore(
    private val db: DB,
) {
    internal val users: HTreeMap<String, String> = db
        .hashMap("users", Serializer.STRING, Serializer.STRING)
        .createOrOpen()

    suspend fun get(
        id: Long,
    ): UserContext? = withContext(Dispatchers.IO) {
        users[id.toString()]?.let {
            Json.decodeFromString(
                UserContext.serializer(),
                it
            )
        }
    }

    suspend fun put(
        userContext: UserContext,
    ) = withContext(Dispatchers.IO) {
        users[userContext.id.toString()] = Json.encodeToString(
            UserContext.serializer(),
            userContext
        )

        db.commit()
    }
}
