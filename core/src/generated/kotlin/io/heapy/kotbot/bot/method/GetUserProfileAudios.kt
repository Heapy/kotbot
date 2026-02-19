package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.UserProfileAudios
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get a list of profile audios for a user. Returns a [UserProfileAudios](https://core.telegram.org/bots/api/#userprofileaudios) object.
 */
@Serializable
public data class GetUserProfileAudios(
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * Sequential number of the first audio to be returned. By default, all audios are returned.
     */
    public val offset: Int? = null,
    /**
     * Limits the number of audios to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    public val limit: Int? = null,
) : Method<GetUserProfileAudios, UserProfileAudios> by Companion {
    public companion object : Method<GetUserProfileAudios, UserProfileAudios> {
        override val _deserializer: KSerializer<Response<UserProfileAudios>> =
                Response.serializer(UserProfileAudios.serializer())

        override val _serializer: KSerializer<GetUserProfileAudios> = serializer()

        override val _name: String = "getUserProfileAudios"
    }
}
