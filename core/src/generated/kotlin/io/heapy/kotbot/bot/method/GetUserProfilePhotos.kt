package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.UserProfilePhotos
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Int
import kotlin.Long
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
) : Method<UserProfilePhotos> {
    public override suspend fun Kotbot.execute(): UserProfilePhotos = requestForJson(
        name = "getUserProfilePhotos",
        serialize = {
            json.encodeToString(
                serializer(),
                this@GetUserProfilePhotos
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<UserProfilePhotos>> =
                Response.serializer(UserProfilePhotos.serializer())
    }
}
