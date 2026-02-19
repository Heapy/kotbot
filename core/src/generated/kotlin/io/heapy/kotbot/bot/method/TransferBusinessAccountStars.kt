package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Transfers Telegram Stars from the business account balance to the bot's balance. Requires the *can_transfer_stars* business bot right. Returns *True* on success.
 */
@Serializable
public data class TransferBusinessAccountStars(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Number of Telegram Stars to transfer; 1-10000
     */
    public val star_count: Int,
) : Method<TransferBusinessAccountStars, Boolean> by Companion {
    public companion object : Method<TransferBusinessAccountStars, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<TransferBusinessAccountStars> = serializer()

        override val _name: String = "transferBusinessAccountStars"
    }
}
