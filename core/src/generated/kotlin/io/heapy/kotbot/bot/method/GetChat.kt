package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Chat
import io.heapy.kotbot.bot.model.ChatId
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get up to date information about the chat (current name of the user for one-on-one conversations, current username of a user, group or channel, etc.). Returns a [Chat](https://core.telegram.org/bots/api/#chat) object on success.
 */
@Serializable
public data class GetChat(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
) : Method<GetChat, Chat> by Companion {
    public companion object : Method<GetChat, Chat> {
        override val _deserializer: KSerializer<Response<Chat>> =
                Response.serializer(Chat.serializer())

        override val _serializer: KSerializer<GetChat> = serializer()

        override val _name: String = "getChat"
    }
}
