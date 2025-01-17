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
 * Verifies a user [on behalf of the organization](https://telegram.org/verify#third-party-verification) which is represented by the bot. Returns *True* on success.
 */
@Serializable
public data class VerifyUser(
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * Custom description for the verification; 0-70 characters. Must be empty if the organization isn't allowed to provide a custom verification description.
     */
    public val custom_description: String? = null,
) : Method<VerifyUser, Boolean> by Companion {
    public companion object : Method<VerifyUser, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<VerifyUser> = serializer()

        override val _name: String = "verifyUser"
    }
}
