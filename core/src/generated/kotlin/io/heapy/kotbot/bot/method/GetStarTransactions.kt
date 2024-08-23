package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.StarTransactions
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Returns the bot's Telegram Star transactions in chronological order. On success, returns a [StarTransactions](https://core.telegram.org/bots/api/#startransactions) object.
 */
@Serializable
public data class GetStarTransactions(
    /**
     * Number of transactions to skip in the response
     */
    public val offset: Int? = null,
    /**
     * The maximum number of transactions to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     */
    public val limit: Int? = null,
) : Method<GetStarTransactions, StarTransactions> by Companion {
    public companion object : Method<GetStarTransactions, StarTransactions> {
        override val _deserializer: KSerializer<Response<StarTransactions>> =
                Response.serializer(StarTransactions.serializer())

        override val _serializer: KSerializer<GetStarTransactions> = serializer()

        override val _name: String = "getStarTransactions"
    }
}
