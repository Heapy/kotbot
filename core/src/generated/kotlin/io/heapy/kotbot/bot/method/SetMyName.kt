package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the bot's name. Returns *True* on success.
 */
@Serializable
public data class SetMyName(
    /**
     * New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given language.
     */
    public val name: String? = null,
    /**
     * A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose language there is no dedicated name.
     */
    public val language_code: String? = null,
) : Method<SetMyName, Boolean> by Companion {
    public companion object : Method<SetMyName, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMyName> = serializer()

        override val _name: String = "setMyName"
    }
}
