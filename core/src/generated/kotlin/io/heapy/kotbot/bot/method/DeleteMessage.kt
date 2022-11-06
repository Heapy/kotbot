package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to delete a message, including service messages, with the following limitations:  
 * \- A message can only be deleted if it was sent less than 48 hours ago.  
 * \- Service messages about a supergroup, channel, or forum topic creation can't be deleted.  
 * \- A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.  
 * \- Bots can delete outgoing messages in private chats, groups, and supergroups.  
 * \- Bots can delete incoming messages in private chats.  
 * \- Bots granted *can_post_messages* permissions can delete outgoing messages in channels.  
 * \- If the bot is an administrator of a group, it can delete any message there.  
 * \- If the bot has *can_delete_messages* permission in a supergroup or a channel, it can delete any message there.  
 * Returns *True* on success.
 */
@Serializable
public data class DeleteMessage(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Identifier of the message to delete
     */
    public val message_id: Int,
) : Method<Boolean> {
    public override suspend fun Kotbot.execute(): Boolean = requestForJson(
        name = "deleteMessage",
        serialize = {
            json.encodeToString(
                serializer(),
                this@DeleteMessage
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())
    }
}
