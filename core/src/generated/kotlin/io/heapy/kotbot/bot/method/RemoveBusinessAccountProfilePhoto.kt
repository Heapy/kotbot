package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Removes the current profile photo of a managed business account. Requires the *can_edit_profile_photo* business bot right. Returns *True* on success.
 */
@Serializable
public data class RemoveBusinessAccountProfilePhoto(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Pass *True* to remove the public photo, which is visible even if the main photo is hidden by the business account's privacy settings. After the main photo is removed, the previous profile photo (if present) becomes the main photo.
     */
    public val is_public: Boolean? = null,
) : Method<RemoveBusinessAccountProfilePhoto, Boolean> by Companion {
    public companion object : Method<RemoveBusinessAccountProfilePhoto, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<RemoveBusinessAccountProfilePhoto> = serializer()

        override val _name: String = "removeBusinessAccountProfilePhoto"
    }
}
