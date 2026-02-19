package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns *True* on success.
 */
@Serializable
public data class LeaveChat(
    /**
     * Unique identifier for the target chat or username of the target supergroup or channel (in the format `@channelusername`). Channel direct messages chats aren't supported; leave the corresponding channel instead.
     */
    public val chat_id: ChatId,
) : Method<LeaveChat, Boolean> by Companion {
    public companion object : Method<LeaveChat, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<LeaveChat> = serializer()

        override val _name: String = "leaveChat"
    }
}
