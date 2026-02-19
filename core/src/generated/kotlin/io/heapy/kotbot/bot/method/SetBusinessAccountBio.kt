package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Changes the bio of a managed business account. Requires the *can_change_bio* business bot right. Returns *True* on success.
 */
@Serializable
public data class SetBusinessAccountBio(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * The new value of the bio for the business account; 0-140 characters
     */
    public val bio: String? = null,
) : Method<SetBusinessAccountBio, Boolean> by Companion {
    public companion object : Method<SetBusinessAccountBio, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetBusinessAccountBio> = serializer()

        override val _name: String = "setBusinessAccountBio"
    }
}
