package io.heapy.kotbot.bot

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import java.lang.RuntimeException

public interface ApiMethod<Res> {
    public val name: String
    public fun Kotbot.serialize(): String
    public fun Kotbot.deserialize(data: String): Res
}

@Serializable
public data class ApiResponse<T>(
    public val ok: Boolean,
    public val result: T? = null,
    public val error_code: Int? = null,
    public val description: String? = null,
    public val parameters: ResponseParameters? = null,
)

@Serializable
public data class ResponseParameters(
    public val migrate_to_chat_id: Long?,
    public val retry_after: Int?,
)

@Serializable
public class GetUpdates(
    public val offset: Int? = null,
    public val limit: Int? = null,
    public val timeout: Int? = null,
    public val allowed_updates: List<String>? = null,
) : ApiMethod<List<ApiUpdate>> {
    override val name: String = "getUpdates"

    @Serializable
    public data class Request(
        val offset: Int? = null,
        val limit: Int? = null,
        val timeout: Int? = null,
        val allowed_updates: List<String>? = null,
    )

    override fun Kotbot.serialize(): String {
        return json.encodeToString(
            Request.serializer(),
            Request(
                offset = offset,
                limit = limit,
                timeout = timeout,
                allowed_updates = allowed_updates,
            )
        )
    }

    private val deserializer: KSerializer<ApiResponse<List<ApiUpdate>>> =
        ApiResponse.serializer(ListSerializer(ApiUpdate.serializer()))

    override fun Kotbot.deserialize(data: String): List<ApiUpdate> {
        return json.decodeFromString(deserializer, data).unwrap()
    }
}

internal fun <T> ApiResponse<T>.unwrap(): T {
    if (ok) {
        return result ?: throw KotbotException("Response is ok but result is null")
    } else {
        throw TelegramApiError(
            message = "Telegram API error",
            errorCode = error_code,
            description = description,
            migrateToChatId = parameters?.migrate_to_chat_id,
            retryAfter = parameters?.retry_after,
        )
    }
}

public class TelegramApiError(
    message: String,
    public val errorCode: Int? = null,
    public val description: String? = null,
    public val migrateToChatId: Long? = null,
    public val retryAfter: Int? = null,
) : RuntimeException(message)

public class KotbotException(
    message: String,
) : RuntimeException(message)

@Serializable
public class GetMe : ApiMethod<User> {
    override val name: String = "getMe"

    override fun Kotbot.serialize(): String {
        return ""
    }

    private val deserializer: KSerializer<ApiResponse<User>> =
        ApiResponse.serializer(User.serializer())

    override fun Kotbot.deserialize(data: String): User {
        return json.decodeFromString(deserializer, data).unwrap()
    }
}
