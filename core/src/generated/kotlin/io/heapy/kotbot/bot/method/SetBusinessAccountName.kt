package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Changes the first and last name of a managed business account. Requires the *can_change_name* business bot right. Returns *True* on success.
 */
@Serializable
public data class SetBusinessAccountName(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * The new value of the first name for the business account; 1-64 characters
     */
    public val first_name: String,
    /**
     * The new value of the last name for the business account; 0-64 characters
     */
    public val last_name: String? = null,
) : Method<SetBusinessAccountName, Boolean> by Companion {
    public companion object : Method<SetBusinessAccountName, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetBusinessAccountName> = serializer()

        override val _name: String = "setBusinessAccountName"
    }
}
