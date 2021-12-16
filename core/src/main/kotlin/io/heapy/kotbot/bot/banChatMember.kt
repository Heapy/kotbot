package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import java.time.Instant

public class BanChatMember(
    private val chatId: String,
    private val userId: Int,
    private val untilDate: Instant?,
    private val revokeMessages: Boolean?,
) : ApiMethod<Boolean> {
    @Serializable
    public data class Request(
        /**
         * Unique identifier for the target group or username of the target
         * supergroup or channel (in the format @channelusername)
         */
        val chat_id: String,
        /**
         * Unique identifier of the target user
         */
        val user_id: Int,
        /**
         * Date when the user will be unbanned, unix time. If user is banned
         * for more than 366 days or less than 30 seconds from the current
         * time they are considered to be banned forever. Applied for
         * supergroups and channels only.
         */
        val until_date: Int?,
        /**
         * Pass True to delete all messages from the chat for the user that
         * is being removed. If False, the user will be able to see messages
         * in the group that were sent before the user was removed. Always
         * True for supergroups and channels.
         */
        val revoke_messages: Boolean?,
    )

    private val deserializer: KSerializer<ApiResponse<Boolean>> =
        ApiResponse.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "banChatMember",
            serialize = {
                json.encodeToString(
                    Request.serializer(),
                    Request(
                        chat_id = chatId,
                        user_id = userId,
                        until_date = untilDate?.epochSecond?.toInt(),
                        revoke_messages = revokeMessages
                    )
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
