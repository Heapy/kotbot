package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to unban a previously banned user in a supergroup or channel. The user will **not** return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. By default, this method guarantees that after the call the user is not a member of the chat, but will be able to join it. So if the user is a member of the chat they will also be **removed** from the chat. If you don't want this, use the parameter *only_if_banned*. Returns *True* on success.
 */
@Serializable
public data class UnbanChatMember(
    /**
     * Unique identifier for the target group or username of the target supergroup or channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * Do nothing if the user is not banned
     */
    public val only_if_banned: Boolean? = null,
) : Method<UnbanChatMember, Boolean> by Companion {
    public companion object : Method<UnbanChatMember, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<UnbanChatMember> = serializer()

        override val _name: String = "unbanChatMember"
    }
}
