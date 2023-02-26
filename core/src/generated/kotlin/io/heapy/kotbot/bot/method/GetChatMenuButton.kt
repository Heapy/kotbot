package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MenuButton
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button. Returns [MenuButton](https://core.telegram.org/bots/api/#menubutton) on success.
 */
@Serializable
public data class GetChatMenuButton(
    /**
     * Unique identifier for the target private chat. If not specified, default bot's menu button will be returned
     */
    public val chat_id: Long? = null,
) : Method<GetChatMenuButton, MenuButton> by Companion {
    public companion object : Method<GetChatMenuButton, MenuButton> {
        override val _deserializer: KSerializer<Response<MenuButton>> =
                Response.serializer(MenuButton.serializer())

        override val _serializer: KSerializer<GetChatMenuButton> = serializer()

        override val _name: String = "getChatMenuButton"
    }
}
