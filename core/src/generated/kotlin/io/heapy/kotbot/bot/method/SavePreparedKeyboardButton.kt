package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.KeyboardButton
import io.heapy.kotbot.bot.model.PreparedKeyboardButton
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Stores a keyboard button that can be used by a user within a Mini App. Returns a [PreparedKeyboardButton](https://core.telegram.org/bots/api/#preparedkeyboardbutton) object.
 */
@Serializable
public data class SavePreparedKeyboardButton(
    /**
     * Unique identifier of the target user that can use the button
     */
    public val user_id: Long,
    /**
     * A JSON-serialized object describing the button to be saved. The button must be of the type *request_users*, *request_chat*, or *request_managed_bot*
     */
    public val button: KeyboardButton,
) : Method<SavePreparedKeyboardButton, PreparedKeyboardButton> by Companion {
    public companion object : Method<SavePreparedKeyboardButton, PreparedKeyboardButton> {
        override val _deserializer: KSerializer<Response<PreparedKeyboardButton>> =
                Response.serializer(PreparedKeyboardButton.serializer())

        override val _serializer: KSerializer<SavePreparedKeyboardButton> = serializer()

        override val _name: String = "savePreparedKeyboardButton"
    }
}
