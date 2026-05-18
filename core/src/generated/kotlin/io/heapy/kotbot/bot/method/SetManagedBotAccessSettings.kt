package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to change the access settings of a managed bot. Returns *True* on success.
 */
@Serializable
public data class SetManagedBotAccessSettings(
    /**
     * User identifier of the managed bot whose access settings will be changed
     */
    public val user_id: Long,
    /**
     * Pass *True*, if only selected users can access the bot. The bot's owner can always access it.
     */
    public val is_access_restricted: Boolean,
    /**
     * A JSON-serialized list of up to 10 identifiers of users who will have access to the bot in addition to its owner. Ignored if *is_access_restricted* is false.
     */
    public val added_user_ids: List<Int>? = null,
) : Method<SetManagedBotAccessSettings, Boolean> by Companion {
    public companion object : Method<SetManagedBotAccessSettings, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetManagedBotAccessSettings> = serializer()

        override val _name: String = "setManagedBotAccessSettings"
    }
}
