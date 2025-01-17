package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Removes verification from a user who is currently verified [on behalf of the organization](https://telegram.org/verify#third-party-verification) represented by the bot. Returns *True* on success.
 */
@Serializable
public data class RemoveUserVerification(
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
) : Method<RemoveUserVerification, Boolean> by Companion {
    public companion object : Method<RemoveUserVerification, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<RemoveUserVerification> = serializer()

        override val _name: String = "removeUserVerification"
    }
}
