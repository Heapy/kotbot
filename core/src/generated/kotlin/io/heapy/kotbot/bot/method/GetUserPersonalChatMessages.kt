package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Message
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to get the last messages from the personal chat (i.e., the chat currently added to their profile) of a given user. On success, an array of [Message](https://core.telegram.org/bots/api/#message) objects is returned.
 */
@Serializable
public data class GetUserPersonalChatMessages(
    /**
     * Unique identifier for the target user
     */
    public val user_id: Long,
    /**
     * The maximum number of messages to return; 1-20
     */
    public val limit: Int,
) : Method<GetUserPersonalChatMessages, List<Message>> by Companion {
    public companion object : Method<GetUserPersonalChatMessages, List<Message>> {
        override val _deserializer: KSerializer<Response<List<Message>>> =
                Response.serializer(ListSerializer(Message.serializer()))

        override val _serializer: KSerializer<GetUserPersonalChatMessages> = serializer()

        override val _name: String = "getUserPersonalChatMessages"
    }
}
