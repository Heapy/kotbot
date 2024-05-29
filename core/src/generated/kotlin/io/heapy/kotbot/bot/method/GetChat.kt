package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatFullInfo
import io.heapy.kotbot.bot.model.ChatId
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get up-to-date information about the chat. Returns a [ChatFullInfo](https://core.telegram.org/bots/api/#chatfullinfo) object on success.
 */
@Serializable
public data class GetChat(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
) : Method<GetChat, ChatFullInfo> by Companion {
    public companion object : Method<GetChat, ChatFullInfo> {
        override val _deserializer: KSerializer<Response<ChatFullInfo>> =
                Response.serializer(ChatFullInfo.serializer())

        override val _serializer: KSerializer<GetChat> = serializer()

        override val _name: String = "getChat"
    }
}
