package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InlineQueryResult
import io.heapy.kotbot.bot.model.PreparedInlineMessage
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Stores a message that can be sent by a user of a Mini App. Returns a [PreparedInlineMessage](https://core.telegram.org/bots/api/#preparedinlinemessage) object.
 */
@Serializable
public data class SavePreparedInlineMessage(
    /**
     * Unique identifier of the target user that can use the prepared message
     */
    public val user_id: Long,
    /**
     * A JSON-serialized object describing the message to be sent
     */
    public val result: InlineQueryResult,
    /**
     * Pass *True* if the message can be sent to private chats with users
     */
    public val allow_user_chats: Boolean? = null,
    /**
     * Pass *True* if the message can be sent to private chats with bots
     */
    public val allow_bot_chats: Boolean? = null,
    /**
     * Pass *True* if the message can be sent to group and supergroup chats
     */
    public val allow_group_chats: Boolean? = null,
    /**
     * Pass *True* if the message can be sent to channel chats
     */
    public val allow_channel_chats: Boolean? = null,
) : Method<SavePreparedInlineMessage, PreparedInlineMessage> by Companion {
    public companion object : Method<SavePreparedInlineMessage, PreparedInlineMessage> {
        override val _deserializer: KSerializer<Response<PreparedInlineMessage>> =
                Response.serializer(PreparedInlineMessage.serializer())

        override val _serializer: KSerializer<SavePreparedInlineMessage> = serializer()

        override val _name: String = "savePreparedInlineMessage"
    }
}
