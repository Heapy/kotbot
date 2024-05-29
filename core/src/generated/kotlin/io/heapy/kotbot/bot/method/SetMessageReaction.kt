package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ReactionType
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the chosen reactions on a message. Service messages can't be reacted to. Automatically forwarded messages from a channel to its discussion group have the same available reactions as messages in the channel. Returns *True* on success.
 */
@Serializable
public data class SetMessageReaction(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Identifier of the target message. If the message belongs to a media group, the reaction is set to the first non-deleted message in the group instead.
     */
    public val message_id: Int,
    /**
     * A JSON-serialized list of reaction types to set on the message. Currently, as non-premium users, bots can set up to one reaction per message. A custom emoji reaction can be used if it is either already present on the message or explicitly allowed by chat administrators.
     */
    public val reaction: List<ReactionType>? = null,
    /**
     * Pass *True* to set the reaction with a big animation
     */
    public val is_big: Boolean? = null,
) : Method<SetMessageReaction, Boolean> by Companion {
    public companion object : Method<SetMessageReaction, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMessageReaction> = serializer()

        override val _name: String = "setMessageReaction"
    }
}
