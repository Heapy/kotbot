package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Changes the emoji status for a given user that previously allowed the bot to manage their emoji status via the Mini App method [requestEmojiStatusAccess](https://core.telegram.org/bots/webapps#initializing-mini-apps). Returns *True* on success.
 */
@Serializable
public data class SetUserEmojiStatus(
    /**
     * Unique identifier of the target user
     */
    public val user_id: Long,
    /**
     * Custom emoji identifier of the emoji status to set. Pass an empty string to remove the status.
     */
    public val emoji_status_custom_emoji_id: String? = null,
    /**
     * Expiration date of the emoji status, if any
     */
    public val emoji_status_expiration_date: Int? = null,
) : Method<SetUserEmojiStatus, Boolean> by Companion {
    public companion object : Method<SetUserEmojiStatus, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetUserEmojiStatus> = serializer()

        override val _name: String = "setUserEmojiStatus"
    }
}
