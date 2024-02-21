package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.UserChatBoosts
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the list of boosts added to a chat by a user. Requires administrator rights in the chat. Returns a [UserChatBoosts](https://core.telegram.org/bots/api/#userchatboosts) object.
 */
@Serializable
public data class GetUserChatBoosts(
    /**
     * Unique identifier for the chat or username of the channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
) : Method<GetUserChatBoosts, UserChatBoosts> by Companion {
    public companion object : Method<GetUserChatBoosts, UserChatBoosts> {
        override val _deserializer: KSerializer<Response<UserChatBoosts>> =
                Response.serializer(UserChatBoosts.serializer())

        override val _serializer: KSerializer<GetUserChatBoosts> = serializer()

        override val _name: String = "getUserChatBoosts"
    }
}
