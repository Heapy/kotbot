package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Changes the username of a managed business account. Requires the *can_change_username* business bot right. Returns *True* on success.
 */
@Serializable
public data class SetBusinessAccountUsername(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * The new value of the username for the business account; 0-32 characters
     */
    public val username: String? = null,
) : Method<SetBusinessAccountUsername, Boolean> by Companion {
    public companion object : Method<SetBusinessAccountUsername, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetBusinessAccountUsername> = serializer()

        override val _name: String = "setBusinessAccountUsername"
    }
}
