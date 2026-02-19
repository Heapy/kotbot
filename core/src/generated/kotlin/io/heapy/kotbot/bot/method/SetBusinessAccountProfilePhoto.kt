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
 * Changes the profile photo of a managed business account. Requires the *can_edit_profile_photo* business bot right. Returns *True* on success.
 */
@Serializable
public data class SetBusinessAccountProfilePhoto(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * The new profile photo to set
     */
    public val photo: InputProfilePhoto,
    /**
     * Pass *True* to set the public photo, which will be visible even if the main photo is hidden by the business account's privacy settings. An account can have only one public photo.
     */
    public val is_public: Boolean? = null,
) : Method<SetBusinessAccountProfilePhoto, Boolean> by Companion {
    public companion object : Method<SetBusinessAccountProfilePhoto, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetBusinessAccountProfilePhoto> = serializer()

        override val _name: String = "setBusinessAccountProfilePhoto"
    }
}
