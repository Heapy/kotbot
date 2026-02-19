package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputProfilePhoto
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Changes the profile photo of the bot. Returns *True* on success.
 */
@Serializable
public data class SetMyProfilePhoto(
    /**
     * The new profile photo to set
     */
    public val photo: InputProfilePhoto,
) : Method<SetMyProfilePhoto, Boolean> by Companion {
    public companion object : Method<SetMyProfilePhoto, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetMyProfilePhoto> = serializer()

        override val _name: String = "setMyProfilePhoto"
    }
}
