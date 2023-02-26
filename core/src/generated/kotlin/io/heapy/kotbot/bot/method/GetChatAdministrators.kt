package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatMember
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

/**
 * Use this method to get a list of administrators in a chat, which aren't bots. Returns an Array of [ChatMember](https://core.telegram.org/bots/api/#chatmember) objects.
 */
@Serializable
public data class GetChatAdministrators(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
) : Method<GetChatAdministrators, List<ChatMember>> by Companion {
    public companion object : Method<GetChatAdministrators, List<ChatMember>> {
        override val _deserializer: KSerializer<Response<List<ChatMember>>> =
                Response.serializer(ListSerializer(ChatMember.serializer()))

        override val _serializer: KSerializer<GetChatAdministrators> = serializer()

        override val _name: String = "getChatAdministrators"
    }
}
