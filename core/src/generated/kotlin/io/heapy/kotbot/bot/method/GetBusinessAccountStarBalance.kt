package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.StarAmount
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Returns the amount of Telegram Stars owned by a managed business account. Requires the *can_view_gifts_and_stars* business bot right. Returns [StarAmount](https://core.telegram.org/bots/api/#staramount) on success.
 */
@Serializable
public data class GetBusinessAccountStarBalance(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
) : Method<GetBusinessAccountStarBalance, StarAmount> by Companion {
    public companion object : Method<GetBusinessAccountStarBalance, StarAmount> {
        override val _deserializer: KSerializer<Response<StarAmount>> =
                Response.serializer(StarAmount.serializer())

        override val _serializer: KSerializer<GetBusinessAccountStarBalance> = serializer()

        override val _name: String = "getBusinessAccountStarBalance"
    }
}
