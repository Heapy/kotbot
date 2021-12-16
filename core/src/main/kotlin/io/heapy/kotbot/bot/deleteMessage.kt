package io.heapy.kotbot.bot

import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to delete a message, including service messages, with the following limitations:
 * - A message can only be deleted if it was sent less than 48 hours ago.
 * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
 * - Bots can delete outgoing messages in private chats, groups, and supergroups.
 * - Bots can delete incoming messages in private chats.
 * - Bots granted can_post_messages permissions can delete outgoing messages in channels.
 * - If the bot is an administrator of a group, it can delete any message there.
 * - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
 * Returns True on success.
 */
@Serializable
public data class DeleteMessage(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    private val chat_id: String,
    /**
     * Identifier of the message to delete
     */
    private val message_id: Int,
) : ApiMethod<Boolean> {
    @Transient
    private val deserializer: KSerializer<ApiResponse<Boolean>> =
        ApiResponse.serializer(Boolean.serializer())

    override suspend fun Kotbot.execute(): Boolean {
        return requestForJson(
            name = "deleteMessage",
            serialize = {
                json.encodeToString(
                    serializer(),
                    this@DeleteMessage
                )
            },
            deserialize = {
                json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
            }
        )
    }
}
