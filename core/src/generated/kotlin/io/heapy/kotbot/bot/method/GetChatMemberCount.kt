package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to get the number of members in a chat. Returns *Int* on success.
 */
@Serializable
public data class GetChatMemberCount(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
) : Method<GetChatMemberCount, Int> by Companion {
    public companion object : Method<GetChatMemberCount, Int> {
        override val _deserializer: KSerializer<Response<Int>> =
                Response.serializer(Int.serializer())

        override val _serializer: KSerializer<GetChatMemberCount> = serializer()

        override val _name: String = "getChatMemberCount"
    }
}
