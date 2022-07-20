package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to ban a channel chat in a supergroup or a channel.
 * Until the chat is unbanned, the owner of the banned chat won't be
 * able to send messages on behalf of any of their channels.
 * The bot must be an administrator in the supergroup or channel
 * for this to work and must have the appropriate administrator rights.
 * Returns True on success.
 */
@Serializable
public data class BanChatSenderChat(
    /**
     * Unique identifier for the target chat or username
     * of the target channel (in the format @channelusername)
     */
    private val chat_id: String,
    /**
     * Unique identifier of the target sender chat
     */
    private val sender_chat_id: Long,
) : Method<Boolean> {
    @Transient
    private val deserializer: KSerializer<Response<Boolean>> =
        Response.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "banChatSenderChat",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@BanChatSenderChat
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
