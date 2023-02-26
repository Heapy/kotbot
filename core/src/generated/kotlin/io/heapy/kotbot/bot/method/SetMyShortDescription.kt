package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the bot's short description, which is shown on the bot's profile page and is sent together with the link when users share the bot. Returns *True* on success.
 */
@Serializable
public data class SetMyShortDescription(
    /**
     * New short description for the bot; 0-120 characters. Pass an empty string to remove the dedicated short description for the given language.
     */
    public val short_description: String? = null,
    /**
     * A two-letter ISO 639-1 language code. If empty, the short description will be applied to all users for whose language there is no dedicated short description.
     */
    public val language_code: String? = null,
) : Method<SetMyShortDescription, Boolean> by Companion {
    public companion object : Method<SetMyShortDescription, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMyShortDescription> = serializer()

        override val _name: String = "setMyShortDescription"
    }
}
