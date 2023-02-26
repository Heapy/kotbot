package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty. Returns *True* on success.
 */
@Serializable
public data class SetMyDescription(
    /**
     * New bot description; 0-512 characters. Pass an empty string to remove the dedicated description for the given language.
     */
    public val description: String? = null,
    /**
     * A two-letter ISO 639-1 language code. If empty, the description will be applied to all users for whose language there is no dedicated description.
     */
    public val language_code: String? = null,
) : Method<SetMyDescription, Boolean> by Companion {
    public companion object : Method<SetMyDescription, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMyDescription> = serializer()

        override val _name: String = "setMyDescription"
    }
}
