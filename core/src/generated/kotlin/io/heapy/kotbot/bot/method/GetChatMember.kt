package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatMember
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get information about a member of a chat. The method is only guaranteed to work for other users if the bot is an administrator in the chat. Returns a [ChatMember](https://core.telegram.org/bots/api/#chatmember) object on success.
 */
@Serializable
public data class GetChatMember(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
) : Method<GetChatMember, ChatMember> by Companion {
    public companion object : Method<GetChatMember, ChatMember> {
        override val _deserializer: KSerializer<Response<ChatMember>> =
                Response.serializer(ChatMember.serializer())

        override val _serializer: KSerializer<GetChatMember> = serializer()

        override val _name: String = "getChatMember"
    }
}
