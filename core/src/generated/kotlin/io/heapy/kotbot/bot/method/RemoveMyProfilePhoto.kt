package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Removes the profile photo of the bot. Requires no parameters. Returns *True* on success.
 */
@Serializable
public class RemoveMyProfilePhoto : Method<RemoveMyProfilePhoto, Boolean> by Companion {
    public companion object : Method<RemoveMyProfilePhoto, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<RemoveMyProfilePhoto> = serializer()

        override val _name: String = "removeMyProfilePhoto"
    }
}
