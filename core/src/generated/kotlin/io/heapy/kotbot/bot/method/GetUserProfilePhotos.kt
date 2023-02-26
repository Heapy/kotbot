package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.UserProfilePhotos
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to get a list of profile pictures for a user. Returns a [UserProfilePhotos](https://core.telegram.org/bots/api/#userprofilephotos) object.
 */
@Serializable
public data class GetUserProfilePhotos(
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * Sequential number of the first photo to be returned. By default, all photos are returned.
     */
    public val offset: Int? = null,
    /**
     * Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    public val limit: Int? = 100,
) : Method<GetUserProfilePhotos, UserProfilePhotos> by Companion {
    public companion object : Method<GetUserProfilePhotos, UserProfilePhotos> {
        override val _deserializer: KSerializer<Response<UserProfilePhotos>> =
                Response.serializer(UserProfilePhotos.serializer())

        override val _serializer: KSerializer<GetUserProfilePhotos> = serializer()

        override val _name: String = "getUserProfilePhotos"
    }
}
