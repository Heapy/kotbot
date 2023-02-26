package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MenuButton
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button. Returns *True* on success.
 */
@Serializable
public data class SetChatMenuButton(
    /**
     * Unique identifier for the target private chat. If not specified, default bot's menu button will be changed
     */
    public val chat_id: Long? = null,
    /**
     * A JSON-serialized object for the bot's new menu button. Defaults to [MenuButtonDefault](https://core.telegram.org/bots/api/#menubuttondefault)
     */
    public val menu_button: MenuButton? = null,
) : Method<SetChatMenuButton, Boolean> by Companion {
    public companion object : Method<SetChatMenuButton, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetChatMenuButton> = serializer()

        override val _name: String = "setChatMenuButton"
    }
}
